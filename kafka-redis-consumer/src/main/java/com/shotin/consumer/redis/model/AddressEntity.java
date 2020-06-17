package com.shotin.consumer.redis.model;

import com.shotin.bankaccount.model.kafka.Address;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressEntity {

    private String street;

    private String city;

    private String state;

    public AddressEntity(Address address) {
        this.street = address.getStreet();
        this.city = address.getCity();
        this.state = address.getState();
    }
}
