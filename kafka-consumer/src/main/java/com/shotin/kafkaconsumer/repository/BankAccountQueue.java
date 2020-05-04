package com.shotin.kafkaconsumer.repository;

import com.shotin.bankaccount.model.kafka.BankAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentLinkedQueue;

@Repository
public class BankAccountQueue {
    private final Logger LOG = LoggerFactory.getLogger(BankAccountQueue.class);

    private ConcurrentLinkedQueue<BankAccount> kafkaBankAccountsQueue = new ConcurrentLinkedQueue<>();

    public void push(BankAccount kafkaBankAccount) {
        kafkaBankAccountsQueue.add(kafkaBankAccount);
        LOG.info("kafkaBankAccountQueue.size="+kafkaBankAccountsQueue.size());
    }

    public BankAccount pop() {
        LOG.info("kafkaBankAccountQueue.size="+(kafkaBankAccountsQueue.size()-1));
        return kafkaBankAccountsQueue.poll();
    }

    public boolean isEmpty() {
        return kafkaBankAccountsQueue.isEmpty();
    }

    public int size(){
        return kafkaBankAccountsQueue.size();
    }
}
