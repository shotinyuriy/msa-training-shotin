package com.shotin.kafka.oracleconsumer.converter;

import com.shotin.bankaccount.model.kafka.BankAccount;
import com.shotin.kafka.oracleconsumer.model.BankAccountEntity;
import org.springframework.stereotype.Component;

@Component
public class BankAccountEntityConverter {
    public BankAccountEntity from(BankAccount bankAccount) {
        var bankAccountEntity = new BankAccountEntity();
        bankAccountEntity.setUuid(bankAccount.getUuid());
        bankAccountEntity.setFirstName(bankAccount.getFirstName());
        bankAccountEntity.setLastName(bankAccount.getLastName());
        bankAccountEntity.setPatronymic(bankAccount.getPatronymic());
        bankAccountEntity.setAccountNumber(bankAccount.getAccountNumber());
        if (bankAccount.getAccountType() != null) {
            bankAccountEntity.setAccountType(BankAccountEntity.AccountType.valueOf(bankAccount.getAccountType().name()));
        }
        return bankAccountEntity;
    }
}
