package com.shotin.consumer.redis.config;

import com.shotin.consumer.redis.model.BankAccountInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(
                new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort()));
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    @Bean
    public ReactiveRedisTemplate<String, BankAccountInfoEntity> reactiveRedisTemplate() {

        RedisSerializationContext<String, BankAccountInfoEntity> redisSerializationContext =
                RedisSerializationContext
                        .<String, BankAccountInfoEntity>newSerializationContext(RedisSerializer.string())
                        .build();

        ReactiveRedisTemplate<String, BankAccountInfoEntity> template
                = new ReactiveRedisTemplate<>(redisConnectionFactory(), redisSerializationContext);

        return template;
    }
}
