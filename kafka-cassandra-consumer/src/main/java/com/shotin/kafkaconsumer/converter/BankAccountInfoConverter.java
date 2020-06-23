package com.shotin.kafkaconsumer.converter;

import com.shotin.bankaccount.model.kafka.JoinedBankAccountInfo;
import com.shotin.kafkaconsumer.model.AddressEntity;
import com.shotin.kafkaconsumer.model.BankAccountEntity;
import com.shotin.kafkaconsumer.model.BankAccountInfo;
import org.springframework.stereotype.Component;

@Component
public class BankAccountInfoConverter {

    public BankAccountInfo from(JoinedBankAccountInfo joinedBankAccountInfo) {
        BankAccountEntity bankAccountEntity = new BankAccountEntity(joinedBankAccountInfo.getBankAccount());
        AddressEntity addressEntity = null;
        if (joinedBankAccountInfo.getAddress() != null) {
            addressEntity = new AddressEntity(joinedBankAccountInfo.getAddress());
        }
        return new BankAccountInfo(bankAccountEntity.getUuid(), bankAccountEntity, addressEntity);
    }
}
