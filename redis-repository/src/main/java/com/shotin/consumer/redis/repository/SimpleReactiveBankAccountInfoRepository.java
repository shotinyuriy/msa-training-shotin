package com.shotin.consumer.redis.repository;

import com.shotin.consumer.redis.model.BankAccountInfoEntity;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.UUID;

/**
 * Implements the ReactiveCrudRepository for BankAccountInfoEntity
 *
 * The BankAccountInfoEntity is serialized to a RedisHash = Java Map&lt;String, String&gt;
 * Serialization to Map and deserialization to BankAccountInfoEntity implemented in the
 * BankAccountInfoEntity itself
 *
 */
public class SimpleReactiveBankAccountInfoRepository
        implements ReactiveCrudRepository<BankAccountInfoEntity, UUID>, ReactiveBankAccountInfoRepository {

    private static final int UUID_LENGTH = 36;
    private final Logger LOG = LoggerFactory.getLogger(SimpleReactiveBankAccountInfoRepository.class);

    private final RedisSerializationContext<String, String> stringKeySerializationContext =
            RedisSerializationContext.<String, String>newSerializationContext(RedisSerializer.string()).build();

    private final ReactiveRedisTemplate<String, BankAccountInfoEntity> reactiveRedisTemplate;

    private String keyForSet() {
        return BankAccountInfoEntity.BANK_ACCOUNT_INFO_KEY;
    }

    private String keyForHash(UUID uuid) {
        return keyForSet() + ":" + uuid;
    }

    public SimpleReactiveBankAccountInfoRepository(ReactiveRedisTemplate<String, BankAccountInfoEntity> reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    @Override
    public <S extends BankAccountInfoEntity> Flux<S> saveAll(Iterable<S> entities) {
        return Flux.fromIterable(entities)
                .flatMap(entity -> save(entity));
    }

    @Override
    public <S extends BankAccountInfoEntity> Flux<S> saveAll(Publisher<S> entityStream) {
        return Flux.from(entityStream)
                .flatMap(entity -> save(entity));
    }

    @Override
    public Mono<BankAccountInfoEntity> findById(UUID uuid) {
        return reactiveRedisTemplate.opsForHash()
                .entries(keyForHash(uuid))
                .collect(() -> new HashMap<Object, Object>(), (map, entry) -> {
                    map.put(entry.getKey(), entry.getValue());
                })
                .map(map -> new BankAccountInfoEntity(map))
                .onErrorResume(ex -> Mono.empty());
    }

    @Override
    public Mono<BankAccountInfoEntity> findById(Publisher<UUID> id) {
        return Mono.from(id)
                .flatMap(uuid -> findById(uuid));
    }

    @Override
    public Mono<Boolean> existsById(UUID uuid) {
        return reactiveRedisTemplate.opsForSet(stringKeySerializationContext)
                .isMember(keyForSet(), uuid);
    }

    @Override
    public Mono<Boolean> existsById(Publisher<UUID> id) {
        return Mono.from(id)
                .flatMap(uuid -> existsById(uuid));
    }

    @Override
    public Flux<BankAccountInfoEntity> findAll() {
        return reactiveRedisTemplate.opsForSet(stringKeySerializationContext)
                .members(keyForSet())
                .filter(hashKey -> hashKey.length() == UUID_LENGTH)
                .flatMap(hashKey -> findById(UUID.fromString(hashKey)));
    }

    @Override
    public Flux<BankAccountInfoEntity> findAllById(Iterable<UUID> uuids) {
        return Flux.fromIterable(uuids)
                .flatMap(uuid -> findById(uuid));
    }

    @Override
    public Flux<BankAccountInfoEntity> findAllById(Publisher<UUID> idStream) {
        return Flux.from(idStream)
                .flatMap(uuid -> findById(uuid));
    }

    @Override
    public Mono<Long> count() {
        return reactiveRedisTemplate.opsForSet(stringKeySerializationContext)
                .size(keyForSet());
    }

    @Override
    public <S extends BankAccountInfoEntity> Mono<S> save(S bankAccountInfoEntity) {
        final String keyForHash = keyForHash(bankAccountInfoEntity.getUuid());
        if (bankAccountInfoEntity.getUuid() == null) {
            return Mono.error(new Exception("UUID is null"));
        }
        return reactiveRedisTemplate.opsForHash()
                .putAll(keyForHash, bankAccountInfoEntity.asMap())
                .flatMap(success -> {
                    if (success) {
                        return reactiveRedisTemplate.opsForSet(stringKeySerializationContext)
                                .add(keyForSet(), bankAccountInfoEntity.getUuid().toString());
                    } else {
                        return Mono.error(new Exception("Saving to Redis Hash Failed"));
                    }
                })
                .flatMap(result -> Long.valueOf(1).equals(result)
                        ? Mono.just(bankAccountInfoEntity)
                        : Mono.error(new Exception("Saving to Redis Set Failed")));
    }

    @Override
    public Mono<Void> deleteById(UUID uuid) {
        final String keyForHash = keyForHash(uuid);
        return reactiveRedisTemplate.opsForHash()
                .delete(keyForHash)
                .flatMap(success -> Boolean.TRUE.equals(success)
                        ? reactiveRedisTemplate.opsForSet(stringKeySerializationContext).remove(keyForSet(), uuid.toString())
                        : Mono.just(0L))
                .then();
    }

    @Override
    public Flux<UUID> findAllKeys() {
        return reactiveRedisTemplate.opsForSet(stringKeySerializationContext)
                .members(keyForSet())
                .filter(key -> key.length() == UUID_LENGTH)
                .map(key -> UUID.fromString(key));
    }

    @Override
    public Mono<Void> deleteById(Publisher<UUID> id) {
        return Mono.from(id)
                .flatMap(uuid -> deleteById(uuid));
    }

    @Override
    public Mono<Void> delete(BankAccountInfoEntity entity) {
        return deleteById(entity.getUuid());
    }

    @Override
    public Mono<Void> deleteAll(Iterable<? extends BankAccountInfoEntity> entities) {
        return Flux.fromIterable(entities)
                .flatMap(entity -> delete(entity))
                .then();
    }

    @Override
    public Mono<Void> deleteAll(Publisher<? extends BankAccountInfoEntity> entityStream) {
        return Flux.from(entityStream)
                .flatMap(entity -> delete(entity))
                .then();
    }

    @Override
    public Mono<Void> deleteAll() {
        return reactiveRedisTemplate.opsForSet(stringKeySerializationContext)
                .members(keyForSet())
                .filter(hashKey -> hashKey.length() == UUID_LENGTH)
                .flatMap(hashKey -> deleteById(UUID.fromString(hashKey)))
                .then();
    }
}