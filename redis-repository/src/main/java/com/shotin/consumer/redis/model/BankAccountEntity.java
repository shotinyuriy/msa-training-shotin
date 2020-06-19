package com.shotin.consumer.redis.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@NoArgsConstructor
public class BankAccountEntity {

    @Id
    protected UUID uuid;

    protected String lastName;

    protected String firstName;

    protected String patronymic;

    protected long accountNumber;

    protected AccountType accountType;
}
