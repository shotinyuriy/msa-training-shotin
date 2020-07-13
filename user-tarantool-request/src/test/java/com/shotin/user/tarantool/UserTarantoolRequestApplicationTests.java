package com.shotin.user.tarantool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.tarantool.Iterator;
import org.tarantool.TarantoolClient;
import org.tarantool.schema.TarantoolSpaceMeta;

import java.util.Collections;
import java.util.List;

@SpringBootTest
class UserTarantoolRequestApplicationTests {

	Logger LOG = LoggerFactory.getLogger(UserTarantoolRequestApplicationTests.class);

	@Autowired
	TarantoolClient tarantoolClient;

	@Test
	void contextLoads() {
		LOG.error("STARTING");

		Assertions.assertNotNull(tarantoolClient);

		tarantoolClient.syncOps().ping();

		TarantoolSpaceMeta spaceMeta = tarantoolClient.getSchemaMeta().getSpace("tester1");
		if (spaceMeta != null && spaceMeta.getName() != null) {
			List<TarantoolSpaceMeta.SpaceField> fields = spaceMeta.getFormat();
			tarantoolClient.syncOps().eval("box.space.tester1:drop()");
		}

		tarantoolClient.syncOps().eval("box.schema.space.create('tester1',{id=1000})");

		tarantoolClient.syncOps().ping();

		tarantoolClient.syncOps().eval("box.space.tester1:format({" +
				" {name = 'id', type = 'unsigned'}," +
				" {name = 'band_name', type = 'string'}," +
				" {name = 'year', type = 'unsigned'}" +
				" })");

		tarantoolClient.syncOps().eval("box.space.tester:create_index('primary', {type = 'hash', parts = {1, 'unsigned'}})");

		Object response = tarantoolClient
				.syncOps()
				.select("tester1", "primary", Collections.singletonList(1), 0, 100, Iterator.EQ);

		LOG.error("RESPONSE = "+response);

		tarantoolClient.syncOps().eval("box.space.tester:drop()");

	}

}
