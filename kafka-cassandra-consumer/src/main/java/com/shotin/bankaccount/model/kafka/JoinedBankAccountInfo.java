package com.shotin.bankaccount.model.kafka;

import java.util.Objects;
import java.util.UUID;

public class JoinedBankAccountInfo {
    private UUID uuid;
    private BankAccount bankAccount;
    private Address address;

    public JoinedBankAccountInfo() {
        
    }

    public JoinedBankAccountInfo(UUID uuid, BankAccount bankAccount, Address address) {
        this.uuid = uuid;
        this.bankAccount = bankAccount;
        this.address = address;
    }

    public UUID getUuid() {
        return uuid;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinedBankAccountInfo that = (JoinedBankAccountInfo) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
