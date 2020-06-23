package com.shotin.kafkaconsumer.model;

import com.datastax.driver.core.DataType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

import static com.shotin.kafkaconsumer.model.BankAccountInfo.BANK_ACCOUNT_INFO_TABLE;

@Table(BANK_ACCOUNT_INFO_TABLE)
public class BankAccountInfo {

    public static final String BANK_ACCOUNT_INFO_TABLE = "bank_account_info";

    @PrimaryKey
    private UUID uuid;

    @CassandraType(type= DataType.Name.UDT, userTypeName = BankAccountEntity.BANK_ACCOUNT_TYPE)
    private BankAccountEntity bank_account;

    @CassandraType(type= DataType.Name.UDT, userTypeName = AddressEntity.ADDRESS_TYPE)
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
