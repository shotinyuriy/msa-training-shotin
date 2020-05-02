package com.shotin.kafkaproducer.service;

import com.shotin.kafkaproducer.model.AccountType;
import com.shotin.kafkaproducer.model.BankAccount;
import org.springframework.stereotype.Service;

@Service
public class BankAccountTypeEnricher {

    public BankAccount enrich(BankAccount bankAccount) {
        if (bankAccount != null) {
            if(bankAccount.getAccountNumber() % 3 == 0) {
                bankAccount.setAccountType(AccountType.SAVINGS);
            } else {
                bankAccount.setAccountType(AccountType.CHECKING);
            }
        }
        return bankAccount;
    }
}
