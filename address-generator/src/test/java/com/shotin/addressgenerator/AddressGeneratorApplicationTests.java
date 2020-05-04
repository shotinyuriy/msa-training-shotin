package com.shotin.addressgenerator;

import com.shotin.addressgenerator.stream.BankAccountTableProcessor;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AddressGeneratorApplicationTests {

	@Autowired
	BankAccountTableProcessor bankAccountTableProcessor;

	@Test
	void contextLoads() {
		Assert.assertNotNull(bankAccountTableProcessor.inputTable());
		Assert.assertNotNull(bankAccountTableProcessor.outputTable());
	}

}

