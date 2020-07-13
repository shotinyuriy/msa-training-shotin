package com.shotin.tarantool.repository;

import lombok.RequiredArgsConstructor;
import org.tarantool.TarantoolClient;
import org.tarantool.schema.TarantoolSpaceMeta;
import org.tarantool.schema.TarantoolSpaceNotFoundException;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class TarantoolClientSpaceMetaOps implements TarantoolSpaceMetaOps {

    private final TarantoolClient tarantoolClient;

    @Override
    public Object createSpace(String spaceName, int id, boolean replaceIfExists) {
        try {
            TarantoolSpaceMeta spaceMeta = tarantoolClient.getSchemaMeta().getSpace(spaceName);

            if (replaceIfExists && spaceMeta != null && spaceMeta.getName() != null) {
                List<TarantoolSpaceMeta.SpaceField> fields = spaceMeta.getFormat();
                tarantoolClient.syncOps().eval("box.space." + spaceName + ":drop()");
            }
        } catch (TarantoolSpaceNotFoundException e) {

        }
        return tarantoolClient.syncOps().eval("box.schema.space.create('"+spaceName+"',{id="+id+"})");
    }

    @Override
    public Object formatSpace(String spaceName, LinkedHashMap<String, FieldType> format) {
        tarantoolClient.syncOps().ping();

        StringBuilder sb = new StringBuilder();
        sb.append("box.space.").append(spaceName).append(":format({");
        Iterator<Map.Entry<String, FieldType>> fields = format.entrySet().iterator();
        while (fields.hasNext()) {
            Map.Entry<String, FieldType> field = fields.next();
            sb.append("{name='").append(field.getKey()).append("',");
            sb.append(" type='").append(field.getValue().toString().toLowerCase()).append("'}");
            if (fields.hasNext()) {
                sb.append(",");
            }
        }
        sb.append("})");

        return tarantoolClient.syncOps().eval(sb.toString());
    }

    @Override
    public Object createPrimaryIndex(String spaceName, Map<Object, FieldType> parts, IndexType indexType) {
        StringBuilder sb = new StringBuilder();
        sb.append("box.space.").append(spaceName)
                .append(":create_index('primary',")
                .append(" {type = ").append(indexType).append(",");
        sb.append(constructParts(parts));
        sb.append("})");
        return tarantoolClient.syncOps().eval(sb.toString());
    }

    @Override
    public Object createSecondaryIndex(String spaceName, String indexName, Map<Object, FieldType> parts, IndexType indexType, boolean unique) {
        StringBuilder sb = new StringBuilder();
        sb.append("box.space.").append(spaceName)
                .append(":create_index('").append(indexName).append("',")
                .append(" {type = '").append(indexType).append("',");
        sb.append(" unique=").append(unique).append(",");
        sb.append(constructParts(parts));
        sb.append("})");
        return tarantoolClient.syncOps().eval(sb.toString());
    }

    public StringBuilder constructParts(Map<Object, FieldType> parts) {
        StringBuilder sb = new StringBuilder();
        sb.append(" parts={");
        Iterator<Map.Entry<Object, FieldType>> partsIter = parts.entrySet().iterator();
        while (partsIter.hasNext()) {
            Map.Entry<Object, FieldType> part = partsIter.next();
            Object key = part.getKey();
            if (key instanceof Integer) {
                sb.append(key);
                sb.append(",").append("'").append(part.getValue().toString()).append("'");
            } else if (key instanceof String) {
                sb.append("'").append(key).append("'");
            }
            if (partsIter.hasNext()) {
                sb.append(",");
            }
        }
        sb.append("}");
        return sb;
    }

    @Override
    public Object dropSpace(String spaceName) {
        return tarantoolClient.syncOps().eval("box.space."+spaceName+":drop()");
    }
}
