package com.shotin.grpc.config;

import com.shotin.grpc.repository.BankAccountInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DataCenterReplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.Collections;
import java.util.List;

/**
 * Cassandra cluster is method is already implemented in {@link org.springframework.data.cassandra.config.AbstractClusterConfiguration}
 *
 * Overriding required methods to replace empty configurations with real values from CassandraProperties
 * provided in the Spring Boot configuration file
 */
@Configuration
@EnableCassandraRepositories(basePackageClasses = {BankAccountInfoRepository.class})
public class CassandraConfig extends AbstractReactiveCassandraConfiguration {

    @Autowired
    private CassandraProperties cassandraProperties;

    @Override
    protected String getKeyspaceName() {
        return cassandraProperties.getKeyspaceName();
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    protected String getLocalDataCenter() {
        return cassandraProperties.getLocalDatacenter();
    }

    /**
     * Use this for custom username and password to connect to Cassandra
     * @param properties
     * @return
     */
    @Bean
    public CqlSessionBuilderCustomizer authCustomizer(final CassandraProperties properties) {
        return (builder) -> builder
                .withKeyspace(properties.getKeyspaceName())
                .withLocalDatacenter(properties.getLocalDatacenter())
                .withAuthCredentials(properties.getUsername(), properties.getPassword());
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        return Collections.singletonList(createKeyspaceSpecification());
    }

    protected CreateKeyspaceSpecification createKeyspaceSpecification() {
        CreateKeyspaceSpecification ckss = CreateKeyspaceSpecification.createKeyspace(getKeyspaceName());
        DataCenterReplication dcr = DataCenterReplication.of(cassandraProperties.getLocalDatacenter(), 3L);
        ckss.ifNotExists(true).withNetworkReplication(dcr);
        return ckss;
    }
}
