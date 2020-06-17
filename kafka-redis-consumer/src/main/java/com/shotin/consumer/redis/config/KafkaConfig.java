package com.shotin.consumer.redis.config;

import com.shotin.bankaccount.model.kafka.BankAccount;
import com.shotin.bankaccount.model.kafka.JoinedBankAccountInfo;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.record.CompressionType;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory consumerFactory() {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildConsumerProperties());
        JsonDeserializer<BankAccount> jsonDeserializer = new JsonDeserializer<>(BankAccount.class);
        jsonDeserializer.addTrustedPackages(BankAccount.class.getPackage().getName());
        return new DefaultKafkaConsumerFactory(config, new UUIDDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<UUID, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory listenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory();
        listenerContainerFactory.setConsumerFactory(consumerFactory());
        return listenerContainerFactory;
    }

    @Bean
    public ProducerFactory<UUID, JoinedBankAccountInfo> producerFactory() {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildProducerProperties());
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<UUID, JoinedBankAccountInfo> kafkaTemplate() {
        return new KafkaTemplate<UUID, JoinedBankAccountInfo>(producerFactory());
    }
}
