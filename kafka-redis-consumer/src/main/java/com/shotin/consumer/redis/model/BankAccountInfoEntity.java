package com.shotin.consumer.redis.model;

import com.shotin.bankaccount.model.kafka.Address;
import com.shotin.bankaccount.model.kafka.BankAccount;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash
@NoArgsConstructor
public class BankAccountInfoEntity {

    private BankAccountEntity bankAccount;

    private AddressEntity address;

    public BankAccountInfoEntity(BankAccountEntity bankAccount, AddressEntity addressEntity) {
        this.bankAccount = bankAccount;
        this.address = address;
    }
}
