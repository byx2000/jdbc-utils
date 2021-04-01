package byx.util.jdbc.core;

/**
 * 行转换器接口
 *
 * @author byx
 */
public interface RowMapper<T> {
    T map(Row row);
}
