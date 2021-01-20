package com.byx;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Hashtable;
import java.util.Map;

public class MapRowMapper implements RowMapper<Map<String, Object>>
{
    @Override
    public Map<String, Object> map(ResultSet rs) throws Exception
    {
        Map<String, Object> map = new Hashtable<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int count = metaData.getColumnCount();
        for (int i = 1; i <= count; i++)
        {
            String key = metaData.getColumnLabel(i);
            Object value = rs.getObject(i);
            map.put(key, value);
        }
        return map;
    }
}
