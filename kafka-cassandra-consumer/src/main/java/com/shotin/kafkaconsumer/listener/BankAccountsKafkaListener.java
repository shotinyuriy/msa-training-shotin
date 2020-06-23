package com.shotin.kafkaconsumer.listener;

import com.shotin.kafkaconsumer.repository.BankAccountQueue;
import com.shotin.bankaccount.model.kafka.BankAccount;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Profile("kafka-listener")
@Component
public class BankAccountsKafkaListener {

    private final Logger LOG = LoggerFactory.getLogger(BankAccountsKafkaListener.class);

    private BankAccountQueue bankAccountQueue;

    public BankAccountsKafkaListener(@Autowired BankAccountQueue bankAccountQueue) {
        this.bankAccountQueue = bankAccountQueue;
    }

    @KafkaListener(topics="${bank-accounts.kafka.topic}")
    public void receiveBankAccounts(ConsumerRecord<UUID, BankAccount> bankAccountRecord) {
        LOG.info("Received bank account record. key="+bankAccountRecord.key()+" value.id="+bankAccountRecord.value().getUuid());
        bankAccountQueue.push(bankAccountRecord.value());
    }
}
