package com.byx.jdbc.core;

import java.sql.ResultSet;

/**
 * 结果集转换器接口
 * @param <T> 转换类型
 */
public interface ResultSetMapper<T>
{
    T map(ResultSet rs) throws Exception;
}
