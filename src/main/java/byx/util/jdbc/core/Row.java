package byx.util.jdbc.core;

/**
 * 数据行：封装了结果集的一行数据。
 * 数据行由若干列组成，每列都是一个值。
 * Row通过Record的getCurrentRow方法获取。
 * Row中包含了一系列getXXX方法用于获取列中的值。
 *
 * @author byx
 *
 * @see Record
 */
public interface Row {
    Object getObject(String columnLabel);

    Object getObject(int columnIndex);

    int getInt(String columnLabel);

    int getInt(int columnIndex);

    String getString(String columnLabel);

    String getString(int columnIndex);

    double getDouble(String columnLabel);

    double getDouble(int columnIndex);

    /**
     * 获取列数
     *
     * @return 列数
     */
    int getColumnCount();

    /**
     * 获取列标签
     *
     * @param index 列索引（从1开始）
     * @return 列标签
     */
    String getColumnLabel(int index);
}
