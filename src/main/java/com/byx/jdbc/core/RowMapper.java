package com.byx.jdbc.core;

import java.sql.ResultSet;

/**
 * 行转换器接口
 * @param <T> 转换类型
 */
public interface RowMapper<T>
{
    T map(ResultSet rs) throws Exception;
}
