package com.shotin.user.tarantool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.tarantool.TarantoolClient;

import java.util.Collections;

@SpringBootTest
class UserTarantoolRequestApplicationTests {

	Logger LOG = LoggerFactory.getLogger(UserTarantoolRequestApplicationTests.class);

	@Autowired
	TarantoolClient tarantoolClient;

	@Test
	void contextLoads() {
		LOG.error("STARTING");

		Assertions.assertNotNull(tarantoolClient);
		Object response = tarantoolClient.syncOps().select("tester", "primary", Collections.singletonList(3), 0, 0, 0);
		LOG.error("RESPONSE = "+response);
	}

}
