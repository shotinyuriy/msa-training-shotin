package com.shotin.userredisrequest.service;

import com.shotin.consumer.redis.model.BankAccountInfoEntity;
import com.shotin.consumer.redis.repository.ReactiveBankAccountInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class BankAccountInfoService {

    private ReactiveBankAccountInfoRepository reactiveBankAccountInfoRepository;

    public BankAccountInfoService(@Autowired ReactiveBankAccountInfoRepository reactiveBankAccountInfoRepository) {
        this.reactiveBankAccountInfoRepository = reactiveBankAccountInfoRepository;
    }

    public Flux<String> findAllKeys() {
        return reactiveBankAccountInfoRepository.findAllKeys()
                .map(UUID::toString);
    }

    public Mono<BankAccountInfoEntity> findById(String uuid) {
        return reactiveBankAccountInfoRepository.findById(UUID.fromString(uuid));
    }
}
