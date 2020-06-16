package com.shotin.kafkaproducer.kafka;

import com.shotin.bankaccount.model.kafka.BankAccount;
import org.apache.kafka.clients.producer.MockProducer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.ProducerFactory;

import java.util.UUID;

public class AsyncAccountPublisherTest {

    private static Logger LOG = LoggerFactory.getLogger(AsyncAccountPublisherTest.class);

    private ProducerFactory<UUID, BankAccount> producerFactory = Mockito.mock(ProducerFactory.class);
    private MockProducer<UUID, BankAccount> mockProducer;
    private AsyncBankAccountPublisher asyncBankAccountPublisher;
    private final String TOPIC = "TOPIC_NAME";

    @BeforeEach
    public void setUp() {
        mockProducer = new MockProducer<>();
        Mockito.when(producerFactory.createProducer()).thenReturn(mockProducer);
        asyncBankAccountPublisher = new AsyncProducerBankAccountPublisher(producerFactory, TOPIC);
    }

    @Test
    public void testSendBankAccount_Success() {
        mockProducer.completeNext();

        BankAccount bankAccount = new BankAccount();

        asyncBankAccountPublisher.sendBankAccount(bankAccount);

        Assertions.assertEquals(1, mockProducer.history().size());
        LOG.info("Metrics = " + mockProducer.metrics());
    }

    @Test
    public void testSendBankAccount_Error() {
        mockProducer.errorNext(new RuntimeException("Test Exception"));

        BankAccount bankAccount = new BankAccount();

        asyncBankAccountPublisher.sendBankAccount(bankAccount);

        Assertions.assertEquals(1, mockProducer.history().size());
        LOG.info("Metrics = " + mockProducer.metrics());
    }
}
