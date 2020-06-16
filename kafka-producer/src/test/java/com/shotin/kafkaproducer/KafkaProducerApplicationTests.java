package com.shotin.kafkaproducer;

import com.shotin.kafkaproducer.kafka.AsyncBankAccountPublisher;
import com.shotin.kafkaproducer.rest.KafkaPublisherResource;
import com.shotin.kafkaproducer.service.BankAccountGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class KafkaProducerApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		AsyncBankAccountPublisher asyncBankAccountPublisher = applicationContext.getBean(AsyncBankAccountPublisher.class);
		BankAccountGenerator bankAccountGenerator = applicationContext.getBean(BankAccountGenerator.class);
		KafkaPublisherResource kafkaMessageResource = applicationContext.getBean(KafkaPublisherResource.class);
	}

}
