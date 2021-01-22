package com.byx.jdbc.mapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 将结果集转换成列表
 * @param <T> 列表元素类型
 */
public class ListResultSetMapper<T> implements ResultSetMapper<List<T>>
{
    private final RowMapper<T> rowMapper;

    public ListResultSetMapper(RowMapper<T> rowMapper)
    {
        this.rowMapper = rowMapper;
    }

    @Override
    public List<T> map(ResultSet rs) throws Exception
    {
        List<T> result = new ArrayList<>();
        while (rs.next())
        {
            result.add(rowMapper.map(rs));
        }
        return result;
    }
}
