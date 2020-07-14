package com.shotin.tarantool.model;

import com.shotin.tarantool.repository.FieldType;
import static com.shotin.tarantool.repository.FieldType.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountInfoTuple implements TarantoolTuple {

    private UUID uuid;

    private String lastName;

    private String firstName;

    private String patronymic;

    private String city;

    private boolean blackListed;

    public static LinkedHashMap<String, FieldType> tupleFormat() {
        LinkedHashMap<String, FieldType> format = new LinkedHashMap<>();
        format.put("uuid", STRING);
        format.put("last_name", STRING);
        format.put("first_name", STRING);
        format.put("patronymic", STRING);
        format.put("city", STRING);
        format.put("black_listed", UNSIGNED);
        return format;
    }

    public static BankAccountInfoTuple from(List<Object> values) {
        BankAccountInfoTuple tuple = new BankAccountInfoTuple();
        tuple.setUuid(UUID.fromString(values.get(0).toString()));
        tuple.setLastName(String.valueOf(values.get(1)));
        tuple.setFirstName(String.valueOf(values.get(2)));
        tuple.setPatronymic(String.valueOf(values.get(3)));
        tuple.setCity(String.valueOf(values.get(4)));
        tuple.setBlackListed(Integer.parseInt(String.valueOf(values.get(5))) > 0);
        return tuple;
    }

    @Override
    public LinkedHashMap<String, FieldType> extractFormat() {
        return tupleFormat();
    }

    @Override
    public Object getId() {
        return uuid;
    }

    @Override
    public List<?> asList() {
        return Arrays.asList(getId().toString(), lastName, firstName, patronymic, city, (blackListed ? 1 : 0));
    }
}
