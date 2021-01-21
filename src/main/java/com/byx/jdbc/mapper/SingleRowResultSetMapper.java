package com.byx.jdbc.mapper;

import java.sql.ResultSet;

public class SingleRowResultSetMapper<T> implements ResultSetMapper<T>
{
    private final Class<T> type;

    public SingleRowResultSetMapper(Class<T> type)
    {
        this.type = type;
    }

    @Override
    public T map(ResultSet rs) throws Exception
    {
        return new BeanRowMapper<>(type).map(rs);
    }
}
