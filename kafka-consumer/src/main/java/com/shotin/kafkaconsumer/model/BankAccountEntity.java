package com.shotin.kafkaconsumer.model;

import com.shotin.kafkaproducer.model.BankAccount;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

import static com.shotin.kafkaconsumer.model.BankAccountEntity.BANK_ACCOUNT_TABLE;

@Table(BANK_ACCOUNT_TABLE)
public class BankAccountEntity {
    public static final String BANK_ACCOUNT_TABLE = "bank_account";

    @PrimaryKey
    private UUID uuid;

    private String firstName;
    private String lastName;
    private String patronymic;
    private long accountNumber;
    private AccountType accountType;

    public BankAccountEntity() {
    }

    public BankAccountEntity(BankAccount bankAccount) {
        this.uuid = bankAccount.getUuid();
        this.accountNumber = bankAccount.getAccountNumber();
        this.firstName = bankAccount.getFirstName();
        this.lastName = bankAccount.getLastName();
        this.patronymic = bankAccount.getPatronymic();
        this.accountType = bankAccount.getAccountType();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "BankAccountEntity{" +
                "uuid=" + uuid +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", accountNumber=" + accountNumber +
                ", accountType=" + accountType +
                '}';
    }
}
