package com.shotin.kafkaconsumer.repository;

import com.shotin.kafkaproducer.model.BankAccount;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BankAccountQueueRepositoryTest {

    private BankAccountQueueRepository bankAccountQueueRepository;

    @Before
    public void setUp() {
         bankAccountQueueRepository = new BankAccountQueueRepository();
    }

    @Test
    public void testPush() {
        bankAccountQueueRepository.push(new BankAccount());
    }

    @Test
    public void testPop() {
        BankAccount bankAccount = bankAccountQueueRepository.pop();
    }

    @Test
    public void testAll() {
        Assert.assertTrue(bankAccountQueueRepository.isEmpty());
        bankAccountQueueRepository.push(new BankAccount());
        Assert.assertFalse(bankAccountQueueRepository.isEmpty());
        BankAccount bankAccount = bankAccountQueueRepository.pop();
        Assert.assertNotNull(bankAccount);
        Assert.assertTrue(bankAccountQueueRepository.isEmpty());
    }
}
