package com.shotin.kafkaconsumer.config;

import com.shotin.kafkaconsumer.model.BankAccountInfo;
import com.shotin.kafkaconsumer.repository.BankAccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DataCenterReplication;
import org.springframework.data.cassandra.core.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.SimpleUserTypeResolver;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.Collections;
import java.util.List;


@Configuration
@EnableCassandraRepositories(
        basePackageClasses = {BankAccountRepository.class}
)
public class TestCassandraConfig extends AbstractCassandraConfiguration {

    protected String keySpaceName = "test_key_space";

    @Override
    protected String getKeyspaceName() {
        return keySpaceName;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{BankAccountInfo.class.getPackage().getName()};
    }

    @Override
    public List<String> getStartupScripts() {
        return CassandraConfig.loadStartUpScriptsFromResource("cassandra-startup-script.sql");
    }

    @Bean
    public CassandraMappingContext mappingContext() throws Exception {
        BasicCassandraMappingContext mappingContext = new BasicCassandraMappingContext();
        mappingContext.setUserTypeResolver(
                new SimpleUserTypeResolver(cluster().getObject(), getKeyspaceName())
        );
        return mappingContext;
    }

    @Override
    public List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        CreateKeyspaceSpecification ckss = CreateKeyspaceSpecification.createKeyspace(getKeyspaceName());
        DataCenterReplication dcr = DataCenterReplication.of("dc1", 3L);
        ckss.ifNotExists(true).withNetworkReplication(dcr);
        return Collections.singletonList(ckss);
    }
}
