package com.byx.jdbc.mapper;

import java.sql.ResultSet;

public interface ResultSetMapper<T>
{
    T map(ResultSet rs) throws Exception;
}
