package com.shotin.blacklist.loader.trantool.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tarantool.TarantoolClient;
import org.tarantool.schema.TarantoolSpaceMeta;

import javax.annotation.PostConstruct;

@Service
public class BankAccountInfoSpaceRepository {

    private final TarantoolClient tarantoolClient;

    private final TarantoolSpaceMetaOps spaceMetaOps;

    public BankAccountInfoSpaceRepository(TarantoolClient tarantoolClient) {
        this.tarantoolClient = tarantoolClient;
        this.spaceMetaOps = new TarantoolClientSpaceMetaOps(tarantoolClient);
    }

    @PostConstruct
    public void init() {

    }
}
