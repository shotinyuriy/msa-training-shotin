package com.shotin.kafkaproducer.kafka;

import com.shotin.bankaccount.model.kafka.BankAccount;

public interface AsyncBankAccountPublisher {

    void sendBankAccount(BankAccount bankAccount);
}
