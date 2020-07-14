package com.shotin.tarantool.model;

import com.shotin.tarantool.repository.FieldType;

import java.util.LinkedHashMap;
import java.util.List;

public interface TarantoolTuple {

    LinkedHashMap<String, FieldType> extractFormat();
    Object getId();
    List<?> asList();
}
