package com.shotin.kafkaproducer.kafka;

import com.shotin.bankaccount.model.kafka.BankAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.UUID;

@Primary
@Service
public class AsyncKafkaTemplateBankAccountPublisher implements AsyncBankAccountPublisher {

    private final Logger LOG = LoggerFactory.getLogger(AsyncKafkaTemplateBankAccountPublisher.class);

    @Autowired
    private KafkaTemplate<UUID, BankAccount> kafkaTemplate;

    @Value("${bank-accounts.kafka.topic}")
    private String topic;

    public AsyncKafkaTemplateBankAccountPublisher(@Autowired KafkaTemplate<UUID, BankAccount> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendBankAccount(BankAccount bankAccount) {

        ListenableFuture<SendResult<UUID, BankAccount>> future = kafkaTemplate.send(topic, bankAccount.getUuid(), bankAccount);
        future.addCallback(new ListenableFutureCallback<SendResult<UUID, BankAccount>>() {

            @Override
            public void onSuccess(SendResult<UUID, BankAccount> result) {
                LOG.info("Successfully pushed message to Kafka topic="+topic);
            }

            @Override
            public void onFailure(Throwable ex) {
                LOG.error("Failed to push message to Kafka topic="+topic, ex);
            }
        });
    }
}
