package com.shotin.consumer.redis.converter;

import com.shotin.bankaccount.model.kafka.JoinedBankAccountInfo;
import com.shotin.consumer.redis.model.AddressEntity;
import com.shotin.consumer.redis.model.BankAccountEntity;
import com.shotin.consumer.redis.model.BankAccountInfoEntity;
import org.springframework.stereotype.Component;

@Component
public class BankAccountInfoConverter {

    public BankAccountInfoEntity from(JoinedBankAccountInfo joinedBankAccountInfo) {
        if (joinedBankAccountInfo.getUuid() ==null || joinedBankAccountInfo.getBankAccount() == null) {
            return null;
        }
        BankAccountEntity bankAccountEntity = new BankAccountEntity(joinedBankAccountInfo.getBankAccount());
        AddressEntity addressEntity = null;
        if (joinedBankAccountInfo.getAddress() != null) {
            addressEntity = new AddressEntity(joinedBankAccountInfo.getAddress());
        }
        BankAccountInfoEntity bankAccountInfoEntity =
                new BankAccountInfoEntity(joinedBankAccountInfo.getUuid(), bankAccountEntity, addressEntity);
        return bankAccountInfoEntity;
    }
}
