package com.shotin.kafkaconsumer.repository;

import com.shotin.bankaccount.model.kafka.BankAccount;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BankAccountQueueTest {

    private BankAccountQueue bankAccountQueue;

    @Before
    public void setUp() {
         bankAccountQueue = new BankAccountQueue();
    }

    @Test
    public void testPush() {
        bankAccountQueue.push(new BankAccount());
    }

    @Test
    public void testPop() {
        BankAccount bankAccount = bankAccountQueue.pop();
    }

    @Test
    public void testAll() {
        Assert.assertTrue(bankAccountQueue.isEmpty());
        bankAccountQueue.push(new BankAccount());
        Assert.assertFalse(bankAccountQueue.isEmpty());
        BankAccount bankAccount = bankAccountQueue.pop();
        Assert.assertNotNull(bankAccount);
        Assert.assertTrue(bankAccountQueue.isEmpty());
    }
}
