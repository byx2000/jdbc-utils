package byx.util.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 转换只有一个列的数据行
 */
public class SingleColumnRowMapper<T> implements RowMapper<T> {
    @Override
    @SuppressWarnings("unchecked")
    public T map(ResultSet rs) throws SQLException {
        return (T) rs.getObject(1);
    }
}
