package com.shotin.tarantool.config;

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
        config.username = "iurii";
        config.password = "Passw0rd";
//        config.connectionTimeout = 1;
        config.initTimeoutMillis = 1000;
        TarantoolClient client = new TarantoolClientImpl("localhost:3301", config);
        return client;
    }
}
