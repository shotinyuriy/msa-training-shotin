package com.shotin.grpc.repository;

import com.shotin.grpc.model.BankAccountInfoEntity;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class MockBankAccountInfoRepository implements BankAccountInfoRepository {
    @Override
    public <S extends BankAccountInfoEntity> Mono<S> insert(S entity) {
        return null;
    }

    @Override
    public <S extends BankAccountInfoEntity> Flux<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends BankAccountInfoEntity> Flux<S> insert(Publisher<S> entities) {
        return null;
    }

    @Override
    public <S extends BankAccountInfoEntity> Mono<S> save(S entity) {
        return null;
    }

    @Override
    public <S extends BankAccountInfoEntity> Flux<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends BankAccountInfoEntity> Flux<S> saveAll(Publisher<S> entityStream) {
        return null;
    }

    @Override
    public Mono<BankAccountInfoEntity> findById(UUID uuid) {
        return null;
    }

    @Override
    public Mono<BankAccountInfoEntity> findById(Publisher<UUID> id) {
        return null;
    }

    @Override
    public Mono<Boolean> existsById(UUID uuid) {
        return null;
    }

    @Override
    public Mono<Boolean> existsById(Publisher<UUID> id) {
        return null;
    }

    @Override
    public Flux<BankAccountInfoEntity> findAll() {
        return null;
    }

    @Override
    public Flux<BankAccountInfoEntity> findAllById(Iterable<UUID> iterable) {
        return null;
    }

    @Override
    public Flux<BankAccountInfoEntity> findAllById(Publisher<UUID> publisher) {
        return null;
    }

    @Override
    public Mono<Long> count() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(UUID uuid) {
        return null;
    }

    @Override
    public Mono<Void> deleteById(Publisher<UUID> id) {
        return null;
    }

    @Override
    public Mono<Void> delete(BankAccountInfoEntity entity) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll(Iterable<? extends BankAccountInfoEntity> entities) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll(Publisher<? extends BankAccountInfoEntity> entityStream) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll() {
        return null;
    }
}
