package com.shotin.bankaccountgenerator;

import com.shotin.bankaccountgenerator.generator.NamesGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class BankAccountGeneratorApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		NamesGenerator manNamesGenerator = applicationContext.getBean("manNamesGenerator", NamesGenerator.class);

		NamesGenerator womanNamesGenerator = applicationContext.getBean( "womanNamesGenerator", NamesGenerator.class);
	}

}
