package com.byx.util.jdbc.core;

import java.util.Hashtable;
import java.util.Map;

/**
 * 将一行数据转换成Map
 */
public class MapRowMapper implements RowMapper<Map<String, Object>>
{
    @Override
    public Map<String, Object> map(Row row)
    {
        Map<String, Object> map = new Hashtable<>();
        int count = row.getColumnCount();
        for (int i = 1; i <= count; i++)
        {
            String key = row.getColumnLabel(i);
            Object value = row.getObject(i);
            map.put(key, value);
        }
        return map;
    }
}
