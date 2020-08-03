package com.shotin.kafkaconsumer.converter;

import com.shotin.bankaccount.model.kafka.Address;
import com.shotin.bankaccount.model.kafka.BankAccount;
import com.shotin.bankaccount.model.kafka.JoinedBankAccountInfo;
import com.shotin.kafkaconsumer.model.AddressEntity;
import com.shotin.kafkaconsumer.model.BankAccountEntity;
import com.shotin.kafkaconsumer.model.BankAccountInfo;
import org.springframework.stereotype.Component;

@Component
public class BankAccountInfoConverter {

    public BankAccountInfo from(JoinedBankAccountInfo joinedBankAccountInfo) {
        BankAccountEntity bankAccountEntity = convertToBankAccountEntity(joinedBankAccountInfo.getBankAccount());
        AddressEntity addressEntity = null;
        if (joinedBankAccountInfo.getAddress() != null) {
            addressEntity = convertToAddressEntity(joinedBankAccountInfo.getAddress());
        }
        return new BankAccountInfo(bankAccountEntity.getUuid(), bankAccountEntity, addressEntity);
    }

    public static BankAccountEntity convertToBankAccountEntity(BankAccount bankAccount) {
        BankAccountEntity ba = new BankAccountEntity();
        ba.setUuid(bankAccount.getUuid());
        ba.setLastName(bankAccount.getLastName());
        ba.setFirstName(bankAccount.getFirstName());
        ba.setPatronymic(bankAccount.getPatronymic());
        ba.setAccountNumber(bankAccount.getAccountNumber());
        ba.setAccountType(bankAccount.getAccountType());
        return ba;
    }

    public static AddressEntity convertToAddressEntity(Address address) {
        AddressEntity a = new AddressEntity();
        a.setStreet(address.getStreet());
        a.setCity(address.getCity());
        a.setState(address.getState());
        return a;
    }
}
