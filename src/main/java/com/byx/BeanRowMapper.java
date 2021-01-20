package com.byx;

import java.sql.ResultSet;

public class BeanRowMapper<T> implements RowMapper<T>
{
    private final Class<T> type;

    public BeanRowMapper(Class<T> type)
    {
        this.type = type;
    }

    @Override
    public T map(ResultSet rs) throws Exception
    {
        T t = type.getDeclaredConstructor().newInstance();
        BeanUtils.populate(t, new MapRowMapper().map(rs));
        return t;
    }
}
