package byx.util.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;

/**
 * 将一行数据转换成Map
 */
public class MapRowMapper implements RowMapper<Map<String, Object>> {
    @Override
    public Map<String, Object> map(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int count = metaData.getColumnCount();
        Map<String, Object> map = new Hashtable<>();

        for (int i = 1; i <= count; i++) {
            String key = metaData.getColumnLabel(i);
            Object value = rs.getObject(i);
            map.put(key, value);
        }

        return map;
    }
}
