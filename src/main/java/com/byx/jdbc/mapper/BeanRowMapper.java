package com.byx.jdbc.mapper;

import java.sql.ResultSet;

/**
 * 将一行数据转换成JavaBean
 * @param <T> 结果类型
 */
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
