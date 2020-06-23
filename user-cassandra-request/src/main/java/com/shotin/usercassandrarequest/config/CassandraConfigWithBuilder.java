package com.shotin.usercassandrarequest.config;

import com.shotin.usercassandrarequest.repository.BankAccountRepository;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackageClasses = {BankAccountRepository.class})
public class CassandraConfigWithBuilder {

    @Bean
    public CqlSessionBuilderCustomizer authCustomizer(final CassandraProperties properties) {
        return (builder) -> builder
                .withKeyspace(properties.getKeyspaceName())
                .withLocalDatacenter(properties.getLocalDatacenter())
                .withAuthCredentials(properties.getUsername(), properties.getPassword());
    }
}
