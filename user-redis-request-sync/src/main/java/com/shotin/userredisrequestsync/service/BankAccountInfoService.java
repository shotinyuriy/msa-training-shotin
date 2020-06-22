package com.shotin.userredisrequestsync.service;

import com.shotin.consumer.redis.model.BankAccountInfoEntity;
import com.shotin.consumer.redis.repository.ReactiveBankAccountInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankAccountInfoService {

    private final ReactiveBankAccountInfoRepository reactiveBankAccountInfoRepository;

    public Flux<String> findAllKeys() {
        return reactiveBankAccountInfoRepository.findAllKeys()
                .map(uuid -> uuid.toString());
    }

    public Mono<BankAccountInfoEntity> findBankAccountInfoById(String uuid) {
        return reactiveBankAccountInfoRepository.findById(UUID.fromString(uuid));
    }
}