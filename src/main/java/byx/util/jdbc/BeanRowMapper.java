package byx.util.jdbc;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 将一行数据转换成JavaBean
 */
public class BeanRowMapper<T> implements RowMapper<T> {
    private final Class<T> type;

    public BeanRowMapper(Class<T> type) {
        this.type = type;
    }

    @Override
    public T map(ResultSet rs) throws SQLException {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            T bean = type.getDeclaredConstructor().newInstance();

            for (int i = 1; i <= count; i++) {
                PropertyDescriptor pd = new PropertyDescriptor(metaData.getColumnLabel(i), type);
                Method setter = pd.getWriteMethod();
                setter.invoke(bean, rs.getObject(i));
            }

            return bean;
        } catch (IntrospectionException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new DbException("error when map row to bean " + type, e);
        }
    }
}
