package com.shotin.kafkaproducer.kafka;

import com.shotin.bankaccount.model.kafka.BankAccount;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Primary
@Service
public class AsyncProducerBankAccountPublisher implements AsyncBankAccountPublisher {

    private final Logger LOG = LoggerFactory.getLogger(AsyncProducerBankAccountPublisher.class);
    private final String topic;
    private final Producer<UUID, BankAccount> producer;
    public AsyncProducerBankAccountPublisher(@Autowired ProducerFactory<UUID, BankAccount> producerFactory,
                                             @Value("${bank-accounts.kafka.topic}") String topic) {
        this.topic = topic;
        this.producer = producerFactory.createProducer();
    }

    public void sendBankAccount(BankAccount bankAccount) {
        ProducerRecord<UUID, BankAccount> bankAccountRecord = new ProducerRecord<>(topic, bankAccount.getUuid(), bankAccount);
        producer.send(bankAccountRecord, (recordMetadata, exception) -> {
            if (exception != null) {
                LOG.error("Failed to push message to Kafka topic="+topic, exception);
            } else {
                LOG.info("Successfully pushed message to Kafka topic="+topic);
            }
        });
    }
}
