package com.shotin.kafka.oracleconsumer;

import com.shotin.kafka.oracleconsumer.repository.AddressRepository;
import com.shotin.kafka.oracleconsumer.repository.BankAccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

@SpringBootTest
class KafkaOracleConsumerApplicationTests {

	@Autowired
	DataSource oracleDataSource;

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	BankAccountRepository bankAccountRepository;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(oracleDataSource);
	}

}
