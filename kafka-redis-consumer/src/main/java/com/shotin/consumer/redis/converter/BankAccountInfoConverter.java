package com.shotin.consumer.redis.converter;

import com.shotin.bankaccount.model.kafka.JoinedBankAccountInfo;
import com.shotin.consumer.redis.model.AddressEntity;
import com.shotin.consumer.redis.model.BankAccountEntity;
import com.shotin.consumer.redis.model.BankAccountInfoEntity;
import org.springframework.stereotype.Component;

@Component
public class BankAccountInfoConverter {

    public BankAccountInfoEntity from(JoinedBankAccountInfo joinedBankAccountInfo) {
        BankAccountEntity bankAccountEntity = new BankAccountEntity(joinedBankAccountInfo.getBankAccount());
        AddressEntity addressEntity = new AddressEntity(joinedBankAccountInfo.getAddress());
        BankAccountInfoEntity bankAccountInfoEntity = new BankAccountInfoEntity(bankAccountEntity, addressEntity);
        return bankAccountInfoEntity;
    }
}
