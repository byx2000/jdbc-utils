package com.byx.jdbc.mapper;

import java.sql.ResultSet;

/**
 * 转换只有一个列的数据行
 * @param <T> 结果类型
 */
public class SingleColumnRowMapper<T> implements RowMapper<T>
{
    private final Class<T> type;

    public SingleColumnRowMapper(Class<T> type)
    {
        this.type = type;
    }

    @Override
    public T map(ResultSet rs) throws Exception
    {
        return type.cast(rs.getObject(1));
    }
}
