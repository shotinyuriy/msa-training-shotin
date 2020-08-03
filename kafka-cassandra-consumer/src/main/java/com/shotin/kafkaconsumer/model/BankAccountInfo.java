package com.shotin.kafkaconsumer.model;

import org.springframework.data.cassandra.core.mapping.*;

import java.util.UUID;

import static com.shotin.kafkaconsumer.model.BankAccountInfo.BANK_ACCOUNT_INFO_TABLE;

@Table(BANK_ACCOUNT_INFO_TABLE)
public class BankAccountInfo {

    public static final String BANK_ACCOUNT_INFO_TABLE = "bank_account_info";

    @PrimaryKey
    private UUID uuid;

    @Frozen
    @Column("bank_account")
    private BankAccountEntity bank_account;

    @Frozen
    @Column("address")
    private AddressEntity address;

    public BankAccountInfo(UUID uuid, BankAccountEntity bank_account, AddressEntity address) {
        this.uuid = uuid;
        this.bank_account = bank_account;
        this.address = address;
    }

    public UUID getUuid() {
        return uuid;
    }

    public BankAccountEntity getBankAccount() {
        return bank_account;
    }

    public AddressEntity getAddress() {
        return address;
    }
}
