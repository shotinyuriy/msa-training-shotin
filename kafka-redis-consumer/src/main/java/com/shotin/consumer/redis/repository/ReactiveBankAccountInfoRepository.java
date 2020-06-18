package com.shotin.consumer.redis.repository;

import com.shotin.consumer.redis.model.BankAccountInfoEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveBankAccountInfoRepository {

    Mono<BankAccountInfoEntity> findById(UUID uuid);
    Flux<BankAccountInfoEntity> findAll();
    Mono<BankAccountInfoEntity> save(BankAccountInfoEntity bankAccountInfoEntity);
    Mono<Void> deleteById(UUID uuid);
}
