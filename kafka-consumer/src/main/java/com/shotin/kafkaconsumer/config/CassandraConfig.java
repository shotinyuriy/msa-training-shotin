package com.shotin.kafkaconsumer.config;

import com.shotin.kafkaconsumer.model.BankAccountEntity;
import com.shotin.kafkaconsumer.repository.BankAccountRepository;
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

/**
 * Cassandra cluster is method is already implemented in {@link org.springframework.data.cassandra.config.AbstractClusterConfiguration}
 *
 * Overriding required methods to replace empty configurations with real values from CassandraProperties
 * provided in the Spring Boot configuration file
 */
@Configuration
@EnableCassandraRepositories(basePackageClasses = {BankAccountRepository.class})
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Autowired
    protected CassandraProperties cassandraProperties;

    @Override
    protected String getKeyspaceName() {
        return cassandraProperties.getKeyspaceName();
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{BankAccountEntity.class.getPackage().getName()};
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.valueOf(cassandraProperties.getSchemaAction().toUpperCase());
    }

    @Override
    public List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        CreateKeyspaceSpecification ckss = CreateKeyspaceSpecification.createKeyspace(getKeyspaceName());
        DataCenterReplication dcr = DataCenterReplication.of("dc1", 3L);
        ckss.ifNotExists(true).withNetworkReplication(dcr);
        return Collections.singletonList(ckss);
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
