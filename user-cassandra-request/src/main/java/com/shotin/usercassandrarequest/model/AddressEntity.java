package com.shotin.usercassandrarequest.model;

import com.datastax.driver.core.DataType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import static com.shotin.usercassandrarequest.model.AddressEntity.ADDRESS_TYPE;

@UserDefinedType(ADDRESS_TYPE)
public class AddressEntity {

    public static final String ADDRESS_TYPE = "address_type";

    @CassandraType(type = DataType.Name.TEXT)
    private String street;

    @CassandraType(type = DataType.Name.TEXT)
    private String city;

    @CassandraType(type = DataType.Name.TEXT)
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
