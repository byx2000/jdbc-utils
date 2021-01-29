package byx.util.jdbc.core;

/**
 * 行转换器接口
 * @param <T> 转换类型
 */
public interface RowMapper<T>
{
    T map(Row row);
}
