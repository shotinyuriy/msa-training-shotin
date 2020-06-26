package com.shotin.kafkaconsumer.config;

import com.shotin.bankaccount.model.kafka.BankAccount;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory consumerFactory() {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildConsumerProperties());
        config.put(ConsumerConfig.CLIENT_ID_CONFIG, "kafka-cassandra-consumer");
        config.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);
        JsonDeserializer<BankAccount> jsonDeserializer = new JsonDeserializer<>(BankAccount.class);
        jsonDeserializer.addTrustedPackages(BankAccount.class.getPackage().getName());
        return new DefaultKafkaConsumerFactory(config, new UUIDDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<UUID, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<UUID, Object> listenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        listenerContainerFactory.setConsumerFactory(consumerFactory());
        return listenerContainerFactory;
    }

    @Bean
    @ConfigurationProperties("kafka.topics")
    public KafkaTopics kafkaTopics() {
        return new KafkaTopics();
    }

    @Bean
    public StreamsBuilderFactoryBean streamsBuilderFactoryBean(@Autowired KafkaStreamsConfiguration streamsConfig) {
        Properties props = streamsConfig.asProperties();
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 1024);
        props.put(StreamsConfig.POLL_MS_CONFIG, 99);
        props.put(StreamsConfig.CLIENT_ID_CONFIG, "kafka-cassandra-consumer");
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 2);
        Map<String, Object> map = new HashMap<>();
        props.forEach((key, value) -> map.put((String)key, value));

        KafkaStreamsConfiguration config = new KafkaStreamsConfiguration(map);

        return new StreamsBuilderFactoryBean(config);
    }
}
