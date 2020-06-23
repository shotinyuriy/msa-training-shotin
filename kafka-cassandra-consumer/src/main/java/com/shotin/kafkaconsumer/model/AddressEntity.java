package com.shotin.kafkaconsumer.model;

import com.datastax.driver.core.DataType;
import com.shotin.bankaccount.model.kafka.Address;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import static com.shotin.kafkaconsumer.model.AddressEntity.ADDRESS_TYPE;

@UserDefinedType(ADDRESS_TYPE)
public class AddressEntity {

    public static final String ADDRESS_TYPE = "address_type";

    @CassandraType(type = DataType.Name.TEXT)
    private String street;

    @CassandraType(type = DataType.Name.TEXT)
    private String city;

    @CassandraType(type = DataType.Name.TEXT)
    private String state;

    public AddressEntity() {

    }

    public AddressEntity(Address address) {
        this.street = address.getStreet();
        this.city = address.getCity();
        this.state = address.getState();
    }

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
