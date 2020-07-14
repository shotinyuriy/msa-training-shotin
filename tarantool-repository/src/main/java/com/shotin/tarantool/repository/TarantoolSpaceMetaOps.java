package com.shotin.tarantool.repository;

import java.util.LinkedHashMap;
import java.util.Map;

public interface TarantoolSpaceMetaOps {

    /**
     * Creates a new space or recreates an existing space depending on the argument in the Tarantool box
     * @param spaceName - name of the space to create or recreate
     * @param id - id of the space
     * @param replaceIfExists - flag to replace an existing space
     * @return
     */
    SpaceResult createSpace(String spaceName, int id, boolean replaceIfExists);

    /**
     * Formats the space with the given schema format
     * @param spaceName
     * @param format - the Map of field name to field type
     * @return
     */
    Object formatSpace(String spaceName, LinkedHashMap<String, FieldType> format);

    Object createPrimaryIndex(String spaceName, LinkedHashMap<Object, FieldType> parts, IndexType indexType);

    Object createSecondaryIndex(String spaceName, String indexName, LinkedHashMap<Object, FieldType> parts,  IndexType indexType, boolean unique);

    /**
     * Drops the given space
     * @param spaceName
     * @return
     */
    Object dropSpace(String spaceName);
}
