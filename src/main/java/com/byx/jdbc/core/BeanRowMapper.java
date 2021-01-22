package com.byx.jdbc.core;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * 将一行数据转换成JavaBean
 * @param <T> 结果类型
 */
public class BeanRowMapper<T> implements RowMapper<T>
{
    private final Class<T> type;

    public BeanRowMapper(Class<T> type)
    {
        this.type = type;
    }

    @Override
    public T map(ResultSet rs) throws Exception
    {
        ResultSetMetaData metaData = rs.getMetaData();
        int count = metaData.getColumnCount();
        T bean = type.getDeclaredConstructor().newInstance();
        for (int i = 1; i <= count; i++)
        {
            PropertyDescriptor pd = new PropertyDescriptor(metaData.getColumnLabel(i), type);
            Method setter = pd.getWriteMethod();
            setter.invoke(bean, rs.getObject(i));
        }
        return bean;
    }
}
