package com.shotin.user.tarantool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tarantool.TarantoolClient;
import org.tarantool.TarantoolClientConfig;
import org.tarantool.TarantoolClientImpl;

@Configuration
public class TarantoolConfig {

    @Bean
    public TarantoolClient tarantoolClient() {
        TarantoolClientConfig config = new TarantoolClientConfig();
//        config.username = "";
//        config.password = "";
        TarantoolClient client = new TarantoolClientImpl("localhost:3301", config);
        return client;
    }
}
