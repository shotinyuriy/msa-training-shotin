package com.shotin.bankaccount.model.kafka;

import java.util.List;

public class BankAccountList {
    private List<BankAccount> bankAccounts;

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    @Override
    public String toString() {
        return "BankAccountList{" +
                "bankAccounts=" + bankAccounts +
                '}';
    }
}