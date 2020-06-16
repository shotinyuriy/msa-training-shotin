package com.shotin.consumer.redis.model;

import com.shotin.bankaccount.model.kafka.Address;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash
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
