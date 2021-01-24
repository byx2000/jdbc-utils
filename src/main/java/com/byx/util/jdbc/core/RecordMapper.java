package com.byx.util.jdbc.core;

/**
 * 结果集转换器接口
 * @param <T> 转换类型
 */
public interface RecordMapper<T>
{
    T map(Record record);
}
