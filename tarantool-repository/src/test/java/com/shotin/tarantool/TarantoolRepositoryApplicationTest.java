package com.shotin.tarantool;

import com.shotin.tarantool.repository.FieldType;
import com.shotin.tarantool.repository.IndexType;
import com.shotin.tarantool.repository.TarantoolClientSpaceMetaOps;
import com.shotin.tarantool.repository.TarantoolSpaceMetaOps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.tarantool.Iterator;
import org.tarantool.TarantoolClient;
import org.tarantool.schema.TarantoolIndexMeta;
import org.tarantool.schema.TarantoolSpaceMeta;
import org.tarantool.schema.TarantoolSpaceNotFoundException;

import java.util.*;

@SpringBootTest
public class TarantoolRepositoryApplicationTest {

    Logger LOG = LoggerFactory.getLogger(TarantoolRepositoryApplicationTest.class);

    @Autowired
    TarantoolClient tarantoolClient;

    @Test
    public void tarantoolClientTest() {
        try {
            LOG.error("STARTING");

            Assertions.assertNotNull(tarantoolClient);

            tarantoolClient.syncOps().ping();

            try {
                TarantoolSpaceMeta spaceMeta = tarantoolClient.getSchemaMeta().getSpace("tester1");
                if (spaceMeta != null && spaceMeta.getName() != null) {
                    List<TarantoolSpaceMeta.SpaceField> fields = spaceMeta.getFormat();
                    tarantoolClient.syncOps().eval("box.space.tester1:drop()");
                }
            } catch (TarantoolSpaceNotFoundException e) {
                // ignore this
            }

            tarantoolClient.syncOps().eval("box.schema.space.create('tester1',{id=1000})");

            tarantoolClient.syncOps().ping();

            tarantoolClient.syncOps().eval("box.space.tester1:format({" +
                    " {name = 'id', type = 'unsigned'}," +
                    " {name = 'band_name', type = 'string'}," +
                    " {name = 'year', type = 'unsigned'}" +
                    " })");

            tarantoolClient.syncOps().eval("box.space.tester1:create_index('primary', {type = 'hash', parts = {1, 'unsigned'}})");

            Object response = tarantoolClient
                    .syncOps()
                    .select("tester1", "primary", Collections.singletonList(1), 0, 100, Iterator.EQ);

            LOG.error("RESPONSE = " + response);

            tarantoolClient.syncOps().eval("box.space.tester1:drop()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tarantoolClientRepositoryTest() {
        final String SPACE_NAME = "tester2";
        TarantoolSpaceMetaOps spaceMetaOps = new TarantoolClientSpaceMetaOps(tarantoolClient);

        spaceMetaOps.createSpace(SPACE_NAME, 1100, true);

        LinkedHashMap<String, FieldType> format = new LinkedHashMap<>();
        format.put("id", FieldType.UNSIGNED);
        format.put("band_name", FieldType.STRING);
        format.put("year", FieldType.UNSIGNED);

        spaceMetaOps.formatSpace(SPACE_NAME, format);

        Map<Object, FieldType> primaryParts = new LinkedHashMap<>();
        primaryParts.put(1, FieldType.UNSIGNED);
        Object primaryIdxResult = spaceMetaOps.createPrimaryIndex(SPACE_NAME, primaryParts, IndexType.HASH);

        Map<Object, FieldType> secondaryParts = new LinkedHashMap<>();
        secondaryParts.put("band_name", FieldType.STRING);
        secondaryParts.put("year", FieldType.UNSIGNED);
        Object secondaryIdxResult = spaceMetaOps.createSecondaryIndex(SPACE_NAME, "band_name_n_year", secondaryParts, IndexType.TREE,false);

        tarantoolClient.syncOps().ping();
        Map<String, TarantoolIndexMeta> indexesMeta = tarantoolClient.getSchemaMeta().getSpace(SPACE_NAME).getIndexes();
        Assertions.assertEquals(2, indexesMeta.size());
        Assertions.assertNotNull(indexesMeta.get("primary"));
        Assertions.assertNotNull(indexesMeta.get("band_name_n_year"));

        tarantoolClient.syncOps().insert(SPACE_NAME, Arrays.asList(1, "ABBA", 1980));
        tarantoolClient.syncOps().insert(SPACE_NAME, Arrays.asList(2, "Metallica", 1990));

        tarantoolClient.syncOps().insert(SPACE_NAME, Arrays.asList(3, "ABBA", 1981));
        tarantoolClient.syncOps().insert(SPACE_NAME, Arrays.asList(4, "Metallica", 1991));

        tarantoolClient.syncOps().insert(SPACE_NAME, Arrays.asList(5, "ABBA", 1980));
        tarantoolClient.syncOps().insert(SPACE_NAME, Arrays.asList(6, "Metallica", 1990));

        List<List<Object>> resultByBandNameAndYear = (List<List<Object>>)tarantoolClient.syncOps().select(
                SPACE_NAME, "band_name_n_year", Arrays.asList("Metallica", 1990), 0, 100, Iterator.EQ);

        Assertions.assertNotNull(resultByBandNameAndYear);
        Assertions.assertEquals(2, resultByBandNameAndYear.size());

        spaceMetaOps.dropSpace(SPACE_NAME);
    }
}
