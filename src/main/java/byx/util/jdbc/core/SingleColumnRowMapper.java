package byx.util.jdbc.core;

/**
 * 转换只有一个列的数据行
 *
 * @author byx
 */
public class SingleColumnRowMapper<T> implements RowMapper<T> {
    private final Class<T> type;

    public SingleColumnRowMapper(Class<T> type) {
        this.type = type;
    }

    @Override
    public T map(Row row) {
        return type.cast(row.getObject(1));
    }
}
