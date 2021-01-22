package com.byx.jdbc.core;

import java.sql.ResultSet;

/**
 * 转换只有一行的结果集
 * @param <T> 结果类型
 */
public class SingleRowResultSetMapper<T> implements ResultSetMapper<T>
{
    private final RowMapper<T> rowMapper;

    public SingleRowResultSetMapper(RowMapper<T> rowMapper)
    {
        this.rowMapper = rowMapper;
    }

    @Override
    public T map(ResultSet rs) throws Exception
    {
        return rowMapper.map(rs);
    }
}
