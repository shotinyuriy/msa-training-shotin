package com.shotin.kafkaproducer.kafka;

import com.shotin.bankaccount.model.kafka.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.UUID;

@SpringBootTest
@EnableKafka
@EmbeddedKafka( brokerProperties = {
        "advertised.listeners=PLAINTEXT_HOST://localhost:9092,PLAINTEXT://localhost:9091",
        "auto.create.topics.enable=true",
        "listener.security.protocol.map=PLAINTEXT_HOST:PLAINTEXT,PLAINTEXT:PLAINTEXT",
        "inter.broker.listener=PLAINTEXT",
        "listeners=PLAINTEXT_HOST://localhost:9092,PLAINTEXT://localhost:9091"
})
public class AsyncAccountPublisherSpringBootTest {

    private static Logger LOG = LoggerFactory.getLogger(AsyncAccountPublisherSpringBootTest.class);

    @Autowired
    ProducerFactory<UUID, BankAccount> producerFactory;

    private AsyncBankAccountPublisher asyncBankAccountPublisher;
    private final String TOPIC = "TOPIC_NAME";

    @BeforeEach
    public void setUp() {
        asyncBankAccountPublisher = new AsyncProducerBankAccountPublisher(producerFactory, TOPIC);
    }

    @Test
    public void testSendBankAccount_Success() {
        BankAccount bankAccount = new BankAccount();

        asyncBankAccountPublisher.sendBankAccount(bankAccount);
    }

    @Test
    public void testSendBankAccount_Error() {
        BankAccount bankAccount = new BankAccount();

        asyncBankAccountPublisher.sendBankAccount(bankAccount);
    }
}
