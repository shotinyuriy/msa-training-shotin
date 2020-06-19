package com.shotin.consumer.redis.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressEntity {

    protected String street;

    protected String city;

    protected String state;
}
