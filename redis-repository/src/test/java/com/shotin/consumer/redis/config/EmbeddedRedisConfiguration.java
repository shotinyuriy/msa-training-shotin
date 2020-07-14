package com.shotin.consumer.redis.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
public class EmbeddedRedisConfiguration {

    private RedisServer redisServer;

    public EmbeddedRedisConfiguration(RedisProperties redisProperties) {
        try {
            this.redisServer = new RedisServer(redisProperties.getPort());
        } catch (IOException e) {
            throw new RuntimeException("Failed to created RedisServer in TestRedisConfiguration",e);
        }
    }

    @PostConstruct
    public void postConstruct() {
        redisServer.start();
    }

    @PreDestroy
    public void preDestroy() {
        redisServer.stop();
    }
}
