package com.shotin.kafkaproducer;

import com.shotin.kafkaproducer.kafka.BankAccountPublisher;
import com.shotin.kafkaproducer.rest.KafkaMessageResource;
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
		BankAccountPublisher bankAccountPublisher = applicationContext.getBean(BankAccountPublisher.class);
		BankAccountGenerator bankAccountGenerator = applicationContext.getBean(BankAccountGenerator.class);
		KafkaMessageResource kafkaMessageResource = applicationContext.getBean(KafkaMessageResource.class);
	}

}
