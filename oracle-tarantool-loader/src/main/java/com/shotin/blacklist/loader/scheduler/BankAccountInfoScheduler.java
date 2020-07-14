package com.shotin.blacklist.loader.scheduler;

import com.shotin.blacklist.loader.oracle.model.BankAccountInfoEntity;
import com.shotin.blacklist.loader.oracle.repository.BankAccountInfoRepository;
import com.shotin.tarantool.model.BankAccountInfoTuple;
import com.shotin.tarantool.repository.BankAccountInfoTarantoolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
@Slf4j
public class BankAccountInfoScheduler {

    private final BankAccountInfoRepository bankAccountInfoRepository;
    private final BankAccountInfoTarantoolRepository bankAccountInfoSpaceRepository;

    @Scheduled(fixedDelay = 30_000)
    public void loadFromOracleToTarantool() {
        log.info("STARTED THE SCHEDULED TASK loadFromOracleToTarantool");
        final AtomicLong entityCount = new AtomicLong(0);
        final AtomicLong tupleCount = new AtomicLong(0);
        List<BankAccountInfoEntity> bankAccountInfoEntities = bankAccountInfoRepository.findAll();
        bankAccountInfoEntities.stream()
                .peek(item -> log.info("ENTITY #" + entityCount.incrementAndGet()
                        +": GOING TO CONVERT ENTITY INTO TUPLE. UUID = "+item.getUuid()))
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
                .peek(item -> log.info("TUPLE #" + tupleCount.incrementAndGet()
                        +": GOING TO SAVE TUPLE TO Tarantool. UUID="+item.getUuid()))
                .forEach(bankAccountInfoSpaceRepository::save);
        log.info("FINISHED THE SCHEDULED TASK loadFromOracleToTarantool");
    }
}
