package com.shotin.consumer.redis;

import com.shotin.consumer.redis.repository.ReactiveBankAccountInfoRepository;
import com.shotin.consumer.redis.repository.SimpleReactiveBankAccountInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

@SpringBootApplication
public class RedisRepositoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisRepositoryApplication.class, args);
    }

    @Bean
    public ReactiveBankAccountInfoRepository reactiveBankAccountInfoRepository(
            @Autowired ReactiveRedisTemplate reactiveRedisTemplate) {

        return new SimpleReactiveBankAccountInfoRepository(reactiveRedisTemplate);
    }

}
