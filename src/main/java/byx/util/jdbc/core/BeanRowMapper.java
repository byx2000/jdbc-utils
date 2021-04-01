package byx.util.jdbc.core;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * 将一行数据转换成JavaBean
 *
 * @author byx
 */
public class BeanRowMapper<T> implements RowMapper<T> {
    private final Class<T> type;

    public BeanRowMapper(Class<T> type) {
        this.type = type;
    }

    @Override
    public T map(Row row) {
        try {
            int count = row.getColumnCount();
            T bean = type.getDeclaredConstructor().newInstance();
            for (int i = 1; i <= count; i++) {
                PropertyDescriptor pd = new PropertyDescriptor(row.getColumnLabel(i), type);
                Method setter = pd.getWriteMethod();
                setter.invoke(bean, row.getObject(i));
            }
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
