package com.shotin.kafkaconsumer.config;

import com.shotin.kafkaconsumer.repository.BankAccountRepository;
import com.shotin.kafkaproducer.model.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DataCenterReplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.Collections;


@Configuration
@EnableCassandraRepositories(
        basePackageClasses = {BankAccountRepository.class, BankAccount.class}
)
public class TestCassandraConfig extends AbstractCassandraConfiguration {

    protected String keySpaceName = "test_key_space";

    @Override
    protected String getKeyspaceName() {
        return keySpaceName;
    }

    @Bean
    public CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints("127.0.0.1");
        cluster.setPort(9142);
        cluster.setKeyspaceCreations(Collections.singletonList(createKeyspaceSpecification()));
        return cluster;
    }

    public CreateKeyspaceSpecification createKeyspaceSpecification() {
        CreateKeyspaceSpecification ckss = CreateKeyspaceSpecification.createKeyspace(getKeyspaceName());
        DataCenterReplication dcr = DataCenterReplication.of("dc1", 3L);
        ckss.ifNotExists(true).createKeyspace(getKeyspaceName()).withNetworkReplication(dcr);
        return ckss;
    }
}
