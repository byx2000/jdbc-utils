package byx.util.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 行转换器
 */
public interface RowMapper<T> {
    T map(ResultSet rs) throws SQLException;
}
