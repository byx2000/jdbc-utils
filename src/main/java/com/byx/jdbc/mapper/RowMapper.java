package com.byx.jdbc.mapper;

import java.sql.ResultSet;

public interface RowMapper<T>
{
    T map(ResultSet rs) throws Exception;
}
