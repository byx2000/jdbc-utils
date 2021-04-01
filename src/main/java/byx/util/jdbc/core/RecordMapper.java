package byx.util.jdbc.core;

/**
 * 结果集转换器接口
 *
 * @author byx
 */
public interface RecordMapper<T> {
    T map(Record record);
}
