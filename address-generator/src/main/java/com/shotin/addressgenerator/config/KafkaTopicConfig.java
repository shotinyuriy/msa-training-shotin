package com.shotin.addressgenerator.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Value("${addresses.kafka.topic:addresses}")
    private String addressesTopic;

    @Value("${bank-accounts.kafka.topic:bank-accounts}")
    private String bankAccountsTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic addressesTopic() {
        Map<String, String> configs = new HashMap<>();
        configs.put(TopicConfig.RETENTION_BYTES_CONFIG, String.valueOf(1024*1024*100));
        configs.put(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(1000*60));
        configs.put(TopicConfig.SEGMENT_BYTES_CONFIG, String.valueOf(1024*1024*10));

        return new NewTopic(addressesTopic, 4, (short) 1)
                .configs(configs);
    }

    @Bean
    public NewTopic bankAccountsTopic() {
        Map<String, String> configs = new HashMap<>();
        configs.put(TopicConfig.RETENTION_BYTES_CONFIG, String.valueOf(1024*1024*100));
        configs.put(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(1000*60));
        configs.put(TopicConfig.SEGMENT_BYTES_CONFIG, String.valueOf(1024*1024*10));

        return new NewTopic(bankAccountsTopic, 4, (short) 1)
                .configs(configs);
    }
}
