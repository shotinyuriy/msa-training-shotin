package com.shotin.tarantool.repository;

import com.shotin.tarantool.model.BankAccountInfoTuple;
import org.springframework.stereotype.Service;
import org.tarantool.Iterator;
import org.tarantool.TarantoolClient;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BankAccountInfoTarantoolRepository {

    private static final String SPACE_NAME = "bank_account_info";
    private static final String FULL_NAME_N_CITY_IDX = "full_name_n_city";

    private final TarantoolClient tarantoolClient;

    private final TarantoolSpaceMetaOps spaceMetaOps;

    public BankAccountInfoTarantoolRepository(TarantoolClient tarantoolClient) {
        this.tarantoolClient = tarantoolClient;
        this.spaceMetaOps = new TarantoolClientSpaceMetaOps(tarantoolClient);
    }

    @PostConstruct
    public void init() {
        SpaceResult spaceResult = spaceMetaOps.createSpace(SPACE_NAME, 100, false);
        if (spaceResult == SpaceResult.NEW || spaceResult == SpaceResult.REPLACED) {
            spaceMetaOps.formatSpace(SPACE_NAME, BankAccountInfoTuple.tupleFormat());

            LinkedHashMap<Object, FieldType> primaryIdxParts = new LinkedHashMap<>();
            primaryIdxParts.put("uuid", FieldType.UNSIGNED);
            spaceMetaOps.createPrimaryIndex(SPACE_NAME, primaryIdxParts, IndexType.HASH);

            LinkedHashMap<Object, FieldType> secondaryIdxParts = new LinkedHashMap<>();
            secondaryIdxParts.put("last_name", FieldType.STRING);
            secondaryIdxParts.put("first_name", FieldType.STRING);
            secondaryIdxParts.put("patronymic", FieldType.STRING);
            secondaryIdxParts.put("city", FieldType.STRING);
            spaceMetaOps.createSecondaryIndex(SPACE_NAME, FULL_NAME_N_CITY_IDX, secondaryIdxParts, IndexType.TREE, false);
        }
    }

    public void save(BankAccountInfoTuple bankAccountInfoTuple) {
        List<?> key = Collections.singletonList(bankAccountInfoTuple.getId().toString());
        tarantoolClient.syncOps().upsert(SPACE_NAME, key, bankAccountInfoTuple.asList());
    }

    public List<BankAccountInfoTuple> findByFullNameAndCity(String lastName, String firstName, String patronymic, String city) {
        List<Object> searchKeys = Arrays.asList(lastName, firstName, patronymic, city);
        List<List<Object>> results = (List<List<Object>>)tarantoolClient.syncOps()
                .select(SPACE_NAME, FULL_NAME_N_CITY_IDX, searchKeys, 0, 10, Iterator.EQ);

        return results.stream()
            .map(BankAccountInfoTuple::from)
                .collect(Collectors.toList());
    }
}
