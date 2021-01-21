package com.byx.jdbc.mapper;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BeanUtils
{
    public static void populate(Object bean, Map<String, ?> properties)
    {
        try
        {
            org.apache.commons.beanutils.BeanUtils.populate(bean, properties);
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }
}
