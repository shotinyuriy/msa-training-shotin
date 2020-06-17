package com.shotin.consumer.redis.model;

import com.shotin.bankaccount.model.kafka.BankAccount;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@NoArgsConstructor
public class BankAccountEntity {

    @Id
    private UUID uuid;

    private String lastName;

    private String firstName;

    private String patronymic;

    private long accountNumber;

    private AccountType accountType;

    public BankAccountEntity(BankAccount bankAccount) {
        this.uuid = bankAccount.getUuid();
        this.lastName = bankAccount.getLastName();
        this.firstName = bankAccount.getFirstName();
        this.patronymic = bankAccount.getPatronymic();
        this.accountNumber = bankAccount.getAccountNumber();
        this.accountType = bankAccount.getAccountType();
    }
}
