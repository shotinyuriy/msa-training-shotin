package com.shotin.usercassandrarequest.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.util.UUID;

import static com.shotin.usercassandrarequest.model.BankAccountEntity.BANK_ACCOUNT_TYPE;

@JsonPropertyOrder({
    "uuid", "lastName", "firstName", "patronymic", "accountNumber", "accountType"
})
@UserDefinedType(BANK_ACCOUNT_TYPE)
public class BankAccountEntity {
    public static final String BANK_ACCOUNT_TYPE = "bank_account_type";

    private UUID uuid;
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;
    @Column("patronymic")
    private String patronymic;
    @Column("account_number")
    private long accountNumber;
    @Column("account_type")
    private AccountType accountType;

    public BankAccountEntity() {
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
