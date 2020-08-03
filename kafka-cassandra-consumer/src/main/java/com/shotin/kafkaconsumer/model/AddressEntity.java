package com.shotin.kafkaconsumer.model;

import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@NoArgsConstructor
@UserDefinedType(AddressEntity.ADDRESS_TYPE)
public class AddressEntity {

    public static final String ADDRESS_TYPE = "address_type";

    @CassandraType(type = CassandraType.Name.VARCHAR)
    private String street;

    @CassandraType(type = CassandraType.Name.VARCHAR)
    private String city;

    @CassandraType(type = CassandraType.Name.VARCHAR)
    private String state;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
