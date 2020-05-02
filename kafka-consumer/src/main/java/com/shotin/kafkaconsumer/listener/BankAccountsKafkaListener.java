package com.shotin.kafkaconsumer.listener;

import com.shotin.kafkaconsumer.repository.BankAccountQueueRepository;
import com.shotin.kafkaproducer.model.BankAccount;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BankAccountsKafkaListener {

    private final Logger LOG = LoggerFactory.getLogger(BankAccountsKafkaListener.class);

    private BankAccountQueueRepository bankAccountQueueRepository;

    public BankAccountsKafkaListener(@Autowired BankAccountQueueRepository bankAccountQueueRepository) {
        this.bankAccountQueueRepository = bankAccountQueueRepository;
    }

    @KafkaListener(topics="${bank-accounts.kafka.topic}")
    public void receiveBankAccounts(ConsumerRecord<String, BankAccount> bankAccountRecord) {
        LOG.info("Received bank account record. key="+bankAccountRecord.key()+" value.id="+bankAccountRecord.value().getUuid());
        bankAccountQueueRepository.push(bankAccountRecord.value());
    }
}
