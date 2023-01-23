package byx.util.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 转换只有一行的结果集
 */
public class SingleRowResultMapper<T> implements ResultMapper<T> {
    private final RowMapper<T> rowMapper;

    public SingleRowResultMapper(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public T map(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return rowMapper.map(rs);
        }
        return null;
    }
}
