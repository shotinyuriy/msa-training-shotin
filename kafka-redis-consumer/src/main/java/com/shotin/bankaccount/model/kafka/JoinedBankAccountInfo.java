package com.shotin.bankaccount.model.kafka;

import java.util.UUID;

public class JoinedBankAccountInfo {
    private UUID uuid;
    private BankAccount bankAccount;
    private Address address;
//    transient private long executionTime;

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

//    public long getExecutionTime() {
//        return executionTime;
//    }
//
//    public void setExecutionTime(long executionTime) {
//        this.executionTime = executionTime;
//    }
}
