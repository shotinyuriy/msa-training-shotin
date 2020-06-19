package com.shotin.consumer.redis.converter;

import com.shotin.bankaccount.model.kafka.Address;
import com.shotin.bankaccount.model.kafka.BankAccount;
import com.shotin.consumer.redis.model.AddressEntity;
import com.shotin.consumer.redis.model.BankAccountEntity;
import org.springframework.stereotype.Component;

@Component
public class AddressConverter {

    public AddressEntity from(Address address) {
        if (address.getUuid() == null) {
            return null;
        }

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setStreet(address.getStreet());
        addressEntity.setCity(address.getCity());
        addressEntity.setState(address.getState());

        return addressEntity;
    }
}
