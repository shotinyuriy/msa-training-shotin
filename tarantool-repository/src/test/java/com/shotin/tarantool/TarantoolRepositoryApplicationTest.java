package com.shotin.tarantool;

import com.shotin.tarantool.model.BankAccountInfoTuple;
import com.shotin.tarantool.repository.*;
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
import java.util.stream.Collectors;

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

        SpaceResult createResult = spaceMetaOps.createSpace(SPACE_NAME, 1100, true);
        SpaceResult createResult2 = spaceMetaOps.createSpace(SPACE_NAME, 1100, false);
        Assertions.assertEquals(SpaceResult.EXISTING, createResult2);

        LinkedHashMap<String, FieldType> format = new LinkedHashMap<>();
        format.put("id", FieldType.UNSIGNED);
        format.put("band_name", FieldType.STRING);
        format.put("year", FieldType.UNSIGNED);

        spaceMetaOps.formatSpace(SPACE_NAME, format);

        LinkedHashMap<Object, FieldType> primaryParts = new LinkedHashMap<>();
        primaryParts.put(1, FieldType.UNSIGNED);
        Object primaryIdxResult = spaceMetaOps.createPrimaryIndex(SPACE_NAME, primaryParts, IndexType.HASH);

        LinkedHashMap<Object, FieldType> secondaryParts = new LinkedHashMap<>();
        secondaryParts.put("band_name", FieldType.STRING);
        secondaryParts.put("year", FieldType.UNSIGNED);
        Object secondaryIdxResult = spaceMetaOps.createSecondaryIndex(SPACE_NAME, "band_name_n_year", secondaryParts, IndexType.TREE, false);

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

        List<List<Object>> resultByBandNameAndYear = (List<List<Object>>) tarantoolClient.syncOps().select(
                SPACE_NAME, "band_name_n_year", Arrays.asList("Metallica", 1990), 0, 100, Iterator.EQ);

        Assertions.assertNotNull(resultByBandNameAndYear);
        Assertions.assertEquals(2, resultByBandNameAndYear.size());

        spaceMetaOps.dropSpace(SPACE_NAME);
    }

    @Test
    public void tarantoolClientBankAccountInfoRepositoryTest() {
        final String SPACE_NAME = "bank_account_info_test";
        final String LAST_NAME_N_CITY_IDX = "last_name_n_city";
        TarantoolSpaceMetaOps spaceMetaOps = new TarantoolClientSpaceMetaOps(tarantoolClient);

        SpaceResult createResult = spaceMetaOps.createSpace(SPACE_NAME, 1100, true);
        SpaceResult createResult2 = spaceMetaOps.createSpace(SPACE_NAME, 1100, false);
        Assertions.assertEquals(SpaceResult.EXISTING, createResult2);

        LinkedHashMap<String, FieldType> format = BankAccountInfoTuple.tupleFormat();

        spaceMetaOps.formatSpace(SPACE_NAME, format);

        LinkedHashMap<Object, FieldType> primaryParts = new LinkedHashMap<>();
        primaryParts.put(1, FieldType.STRING);
        Object primaryIdxResult = spaceMetaOps.createPrimaryIndex(SPACE_NAME, primaryParts, IndexType.HASH);

        LinkedHashMap<Object, FieldType> secondaryParts = new LinkedHashMap<>();
        secondaryParts.put("last_name", FieldType.STRING);
        secondaryParts.put("city", FieldType.STRING);
        Object secondaryIdxResult = spaceMetaOps.createSecondaryIndex(SPACE_NAME, LAST_NAME_N_CITY_IDX, secondaryParts, IndexType.TREE, false);

        tarantoolClient.syncOps().ping();
        Map<String, TarantoolIndexMeta> indexesMeta = tarantoolClient.getSchemaMeta().getSpace(SPACE_NAME).getIndexes();
        Assertions.assertEquals(2, indexesMeta.size());
        Assertions.assertNotNull(indexesMeta.get("primary"));
        Assertions.assertNotNull(indexesMeta.get(LAST_NAME_N_CITY_IDX));

        BankAccountInfoTuple bankAccInfo1 = new BankAccountInfoTuple(UUID.randomUUID(), "LastName", "FirstName", "Patronymic", "City", true);
        BankAccountInfoTuple bankAccInfo2 = new BankAccountInfoTuple(UUID.randomUUID(), "LastName", "FirstName", "Patronymic", "City", false);

        tarantoolClient.syncOps().insert(SPACE_NAME, bankAccInfo1.asList());
        tarantoolClient.syncOps().insert(SPACE_NAME, bankAccInfo2.asList());

        List<List<Object>> resultBySecondaryIdx = (List<List<Object>>) tarantoolClient.syncOps()
                .select(
                        SPACE_NAME, LAST_NAME_N_CITY_IDX, Arrays.asList("LastName", "City"),
                        0, 100, Iterator.EQ);

        Assertions.assertNotNull(resultBySecondaryIdx);
        Assertions.assertEquals(2, resultBySecondaryIdx.size());

        List<BankAccountInfoTuple> bankAccountInfoTuples = resultBySecondaryIdx.stream()
                .map(list -> BankAccountInfoTuple.from(list))
                .collect(Collectors.toList());

        spaceMetaOps.dropSpace(SPACE_NAME);
    }
}
