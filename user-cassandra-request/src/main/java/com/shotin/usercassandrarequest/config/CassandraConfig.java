package com.shotin.usercassandrarequest.config;

import com.shotin.usercassandrarequest.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DataCenterReplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableCassandraRepositories(basePackageClasses = {BankAccountRepository.class})
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keyspaceName;

    @Override
    protected String getKeyspaceName() {
        return keyspaceName;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    public List<String> getStartupScripts() {
        return loadStartUpScriptsFromResource("cassandra-startup-script.sql");
    }

    @Bean
    public CassandraClusterFactoryBean cluster(@Autowired CassandraProperties cassandraProperties) {
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints(cassandraProperties.getContactPoints().get(0));
        cluster.setPort(cassandraProperties.getPort());
        cluster.setKeyspaceCreations(Collections.singletonList(createKeyspaceSpecification()));
        return cluster;
    }

    public CreateKeyspaceSpecification createKeyspaceSpecification() {
        CreateKeyspaceSpecification ckss = CreateKeyspaceSpecification.createKeyspace(getKeyspaceName());
        DataCenterReplication dcr = DataCenterReplication.of("dc1", 3L);
        ckss.ifNotExists(true).withNetworkReplication(dcr);
        return ckss;
    }

    public static List<String> loadStartUpScriptsFromResource(String resourceName) {
        List<String> scripts = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        try(InputStream inputStream = CassandraConfig.class.getClassLoader().getResourceAsStream(resourceName);
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader)) {

            bufferedReader.lines()
                    .forEach(line -> sb.append(line+"\n"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load cassandra startup script", e);
        }
        if (sb.length() > 0) {
            String[] lines = sb.toString().split("\\|");
            for (String line : lines) {
                scripts.add(line);
            }
        }
        return scripts;
    }
}
