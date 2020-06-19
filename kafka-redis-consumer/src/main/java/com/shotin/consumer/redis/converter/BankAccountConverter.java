package com.shotin.consumer.redis.converter;

import com.shotin.bankaccount.model.kafka.BankAccount;
import com.shotin.consumer.redis.model.BankAccountEntity;
import org.springframework.stereotype.Component;

@Component
public class BankAccountConverter {

    public BankAccountEntity from(BankAccount bankAccount) {
        if (bankAccount.getUuid() ==null) {
            return null;
        }
        BankAccountEntity bankAccountEntity = new BankAccountEntity();
        bankAccountEntity.setFirstName(bankAccount.getFirstName());
        bankAccountEntity.setLastName(bankAccount.getLastName());
        bankAccountEntity.setPatronymic(bankAccount.getPatronymic());
        bankAccountEntity.setUuid(bankAccount.getUuid());
        bankAccountEntity.setAccountNumber(bankAccount.getAccountNumber());
        bankAccountEntity.setAccountType(bankAccount.getAccountType());

        return bankAccountEntity;
    }
}
