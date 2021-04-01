package byx.util.jdbc.core;

/**
 * 转换只有一行的结果集
 *
 * @author byx
 */
public class SingleRowRecordMapper<T> implements RecordMapper<T> {
    private final RowMapper<T> rowMapper;

    public SingleRowRecordMapper(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public T map(Record record) {
        if (record.next()) {
            return rowMapper.map(record.getCurrentRow());
        }
        return null;
    }
}
