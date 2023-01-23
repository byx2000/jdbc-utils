package byx.util.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将结果集转换成列表
 */
public class ListResultMapper<T> implements ResultMapper<List<T>> {
    private final RowMapper<T> rowMapper;

    public ListResultMapper(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public List<T> map(ResultSet rs) throws SQLException {
        List<T> result = new ArrayList<>();
        while (rs.next()) {
            result.add(rowMapper.map(rs));
        }
        return result;
    }
}
