package com.shotin.kafka.oracleconsumer.converter;

import com.shotin.bankaccount.model.kafka.Address;
import com.shotin.kafka.oracleconsumer.model.AddressEntity;
import org.springframework.stereotype.Component;

@Component
public class AddressEntityConverter {

    public AddressEntity from(Address address) {
        var addressEntity = new AddressEntity();
        addressEntity.setUuid(address.getUuid());
        addressEntity.setStreet(address.getStreet());
        addressEntity.setCity(address.getCity());
        addressEntity.setState(address.getState());
        return addressEntity;
    }
}
