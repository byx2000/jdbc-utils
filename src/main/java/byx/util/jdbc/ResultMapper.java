package byx.util.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果集转换器
 */
public interface ResultMapper<T> {
    T map(ResultSet rs) throws SQLException;
}
