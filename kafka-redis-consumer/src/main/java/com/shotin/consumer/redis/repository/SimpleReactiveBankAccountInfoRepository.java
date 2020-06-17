package com.shotin.consumer.redis.repository;

import com.shotin.consumer.redis.model.BankAccountInfoEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.UUID;

@Component
public class SimpleReactiveBankAccountInfoRepository implements ReactiveBankAccountInfoRepository {

    final Logger LOG = LoggerFactory.getLogger(SimpleReactiveBankAccountInfoRepository.class);

    final RedisSerializationContext<String, String> stringKeySerializationContext =
            RedisSerializationContext.<String, String>newSerializationContext(RedisSerializer.string()).build();

    @Autowired
    private ReactiveRedisTemplate<String, BankAccountInfoEntity> reactiveRedisTemplate;

    private String keyForSet() {
        return BankAccountInfoEntity.BANK_ACCOUNT_INFO_KEY;
    }

    private String keyForHash(UUID uuid) {
        return keyForSet()+":"+uuid.toString();
    }

    @Override
    public Mono<BankAccountInfoEntity> findById(UUID uuid) {
        return reactiveRedisTemplate.opsForHash()
                .entries(keyForHash(uuid))
                .collect(() -> new HashMap<Object, Object>(), (map, entry) -> {
                    map.put(entry.getKey(), entry.getValue());
                })
//                .doOnEach(map -> LOG.info("MAP: "+map))
                .map(map -> new BankAccountInfoEntity(map));
    }

    @Override
    public Flux<BankAccountInfoEntity> findAll() {
        return reactiveRedisTemplate.opsForSet(stringKeySerializationContext)
                .members(keyForSet())
                .filter(hashKey -> hashKey.length() == 36)
                .flatMap(hashKey -> findById(UUID.fromString(hashKey)));
    }

    @Override
    public Mono<BankAccountInfoEntity> save(BankAccountInfoEntity bankAccountInfoEntity) {
        final String keyForHash = keyForHash(bankAccountInfoEntity.getUuid());
        return reactiveRedisTemplate.opsForHash()
                .putAll(keyForHash, bankAccountInfoEntity.asMap())
                .flatMap(success -> {
                    if (success) {
                        return reactiveRedisTemplate.opsForSet(stringKeySerializationContext)
                                .add(keyForSet(), bankAccountInfoEntity.getUuid().toString());
                    } else {
                        return Mono.error(new Exception("Saving to Hash Failed"));
                    }
                })
                .flatMap(result -> Long.valueOf(1).equals(result)
                        ? Mono.just(bankAccountInfoEntity)
                        : null);
    }

    @Override
    public Mono<Boolean> delete(UUID uuid) {
        final String keyForHash = keyForHash(uuid);
        return reactiveRedisTemplate.opsForHash()
                .delete(keyForHash)
                .flatMap(success -> Boolean.TRUE.equals(success)
                        ? reactiveRedisTemplate.opsForSet(stringKeySerializationContext).remove(keyForSet(), uuid.toString())
                        : Mono.just(0L))
                .flatMap(result -> Mono.just(result != null && result > 0L));
    }
}