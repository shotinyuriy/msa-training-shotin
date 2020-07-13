package com.shotin.tarantool.repository;

import java.util.LinkedHashMap;
import java.util.Map;

public interface TarantoolSpaceMetaOps {

    Object createSpace(String spaceName, int id, boolean replaceIfExists);

    Object formatSpace(String spaceName, LinkedHashMap<String, FieldType> format);

    Object createPrimaryIndex(String spaceName, Map<Object, FieldType> parts, IndexType indexType);

    Object createSecondaryIndex(String spaceName, String indexName, Map<Object, FieldType> parts,  IndexType indexType, boolean unique);

    Object dropSpace(String spaceName);
}
