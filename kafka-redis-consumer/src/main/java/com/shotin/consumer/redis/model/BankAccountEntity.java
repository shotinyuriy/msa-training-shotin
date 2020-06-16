package com.shotin.consumer.redis.model;

import com.shotin.bankaccount.model.kafka.BankAccount;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class BankAccountEntity {

    private UUID uuid;

    private String firstName;

    private String lastName;

    private String patronymic;

    private long accountNumber;

    private AccountType accountType;

    public BankAccountEntity(BankAccount bankAccount) {
        this.uuid = bankAccount.getUuid();
        this.firstName = bankAccount.getFirstName();
        this.lastName = bankAccount.getLastName();
        this.patronymic = bankAccount.getPatronymic();
        this.accountNumber = bankAccount.getAccountNumber();
        this.accountType = bankAccount.getAccountType();
    }
}
