package com.shotin.kafkaproducer.kafka;

import com.shotin.kafkaproducer.model.BankAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class BankAccountPublisher {

    private final Logger LOG = LoggerFactory.getLogger(BankAccountPublisher.class);

    @Autowired
    private KafkaTemplate<String, BankAccount> kafkaTemplate;

    @Value("${bank-accounts.kafka.topic}")
    private String topic;

    public BankAccountPublisher(@Autowired KafkaTemplate<String, BankAccount> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendBankAccount(BankAccount bankAccount) {

        ListenableFuture<SendResult<String, BankAccount>> future = kafkaTemplate.send(topic, bankAccount);
        future.addCallback(new ListenableFutureCallback<SendResult<String, BankAccount>>() {

            @Override
            public void onSuccess(SendResult<String, BankAccount> result) {
                LOG.info("Successfully pushed message to Kafka topic="+topic);
            }

            @Override
            public void onFailure(Throwable ex) {
                LOG.error("Failed to push message to Kafka topic="+topic, ex);
            }
        });
    }
}
