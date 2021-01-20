package com.byx;

import java.sql.ResultSet;

public class SingleColumnResultSetMapper<T> implements ResultSetMapper<T>
{
    @Override
    @SuppressWarnings("all")
    public T map(ResultSet rs) throws Exception
    {
        return rs.next() ? (T) rs.getObject(1) : null;
    }
}
