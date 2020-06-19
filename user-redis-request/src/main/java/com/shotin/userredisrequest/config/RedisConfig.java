package com.shotin.userredisrequest.config;

import com.shotin.consumer.redis.model.BankAccountInfoEntity;
import com.shotin.consumer.redis.repository.SimpleReactiveBankAccountInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisConfiguration redisConfig
                = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        return new LettuceConnectionFactory(redisConfig);
    }

    @Bean
    public ReactiveRedisTemplate<String, BankAccountInfoEntity> reactiveRedisTemplate() {
        RedisSerializationContext<String, BankAccountInfoEntity> redisSerializationContext =
                RedisSerializationContext.<String, BankAccountInfoEntity>newSerializationContext(RedisSerializer.string()).build();

        ReactiveRedisTemplate<String, BankAccountInfoEntity> template
                = new ReactiveRedisTemplate<>(redisConnectionFactory(), redisSerializationContext);

        return template;
    }

    @Bean
    public SimpleReactiveBankAccountInfoRepository reactiveBankAccountInfoRepository() {
        return new SimpleReactiveBankAccountInfoRepository(reactiveRedisTemplate());
    }
}
