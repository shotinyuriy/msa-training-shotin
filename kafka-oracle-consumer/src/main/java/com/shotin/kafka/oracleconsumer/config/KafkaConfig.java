package com.shotin.kafka.oracleconsumer.config;

import com.shotin.bankaccount.model.kafka.BankAccount;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory consumerFactory() {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildConsumerProperties());
        config.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);
        JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages(BankAccount.class.getPackageName());
        return new DefaultKafkaConsumerFactory(config, new UUIDDeserializer(), jsonDeserializer);
    }

    @Bean
    @ConfigurationProperties("kafka.topics")
    public KafkaTopics kafkaTopics() {
        return new KafkaTopics();
    }

    @Bean
    public StreamsBuilderFactoryBean streamsBuilderFactoryBean() {
        Map<String, Object> config = kafkaProperties.buildStreamsProperties();
        KafkaStreamsConfiguration streamsConfiguration = new KafkaStreamsConfiguration(config);
        return new StreamsBuilderFactoryBean(streamsConfiguration);
    }
}
