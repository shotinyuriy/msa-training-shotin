package com.shotin.blacklist.loader.scheduler;

import com.shotin.blacklist.loader.oracle.model.BankAccountInfoEntity;
import com.shotin.blacklist.loader.oracle.repository.BankAccountInfoRepository;
import com.shotin.tarantool.model.BankAccountInfoTuple;
import com.shotin.tarantool.repository.BankAccountInfoTarantoolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class BankAccountInfoScheduler {

    private final BankAccountInfoRepository bankAccountInfoRepository;
    private final BankAccountInfoTarantoolRepository bankAccountInfoSpaceRepository;

    @Scheduled(initialDelay = 15_000, fixedDelay = 30_000)
    public void loadFromOracleToTarantool() {
        log.info("(1) START LOADING");
        long startTotal = System.currentTimeMillis();
        Long updateId = 1L;
        final int pageCount = Runtime.getRuntime().availableProcessors();

        log.info("(1) START COUNTING");
        long startCount = System.currentTimeMillis();
        int count = bankAccountInfoRepository.countByUpdateId(updateId);

        int pageSize = count / pageCount + 1;
        long finishCount = System.currentTimeMillis();
        log.info("(1) FINISH COUNTING PAGE COUNT = "+pageCount+" TIME = "+ (finishCount - startCount)+ " ms");

        long start = System.currentTimeMillis();
        List<PageRequest> pageRequests = new ArrayList<>();
        for(int i = 0; i < pageCount; i++) {
            pageRequests.add(PageRequest.of( i, pageSize));
        }
        log.info("(1) START READING AND WRITING");
        pageRequests
                .parallelStream()
                .forEach(pageRequest -> bankAccountInfoRepository.findAllByUpdateId(updateId, pageRequest)
                        .parallelStream()
                        .map(entity -> {
                            BankAccountInfoTuple tuple = new BankAccountInfoTuple();
                            tuple.setUuid(entity.getUuid());
                            tuple.setLastName(entity.getLastName());
                            tuple.setFirstName(entity.getFirstName());
                            tuple.setPatronymic(entity.getPatronymic());
                            tuple.setCity(entity.getCity());
                            tuple.setBlackListed(Boolean.TRUE.equals(entity.getBlackListed()));
                            return tuple;
                        })
                        .forEach(bankAccountInfoSpaceRepository::save))
                ;
        long finish = System.currentTimeMillis();
        log.info("(1) FINISH READING AND WRITING TIME = "+ (finish - start)+" ms \n TOTAL TIME = "+(finish - startTotal)+ "ms");
    }

    @Scheduled(initialDelay = 0, fixedDelay = 35_000)
    public void loadFromOracleToTarantoolNoPaging() {
        log.info("(2) START LOADING");
        log.info("(2) START READING AND WRITING");
        long start = System.currentTimeMillis();
                bankAccountInfoRepository.findAll()
                .parallelStream()
                .map(entity -> {
                    BankAccountInfoTuple tuple = new BankAccountInfoTuple();
                    tuple.setUuid(entity.getUuid());
                    tuple.setLastName(entity.getLastName());
                    tuple.setFirstName(entity.getFirstName());
                    tuple.setPatronymic(entity.getPatronymic());
                    tuple.setCity(entity.getCity());
                    tuple.setBlackListed(Boolean.TRUE.equals(entity.getBlackListed()));
                    return tuple;
                })
                .forEach(bankAccountInfoSpaceRepository::save);
        long finish = System.currentTimeMillis();
        log.info("(2) FINISH READING AND WRITING TIME = "+ (finish - start)+" ms");
    }
}
