package com.byx.jdbc.core;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class RecordAdapterForResultSet implements Record, Row
{
    private final ResultSet rs;

    public RecordAdapterForResultSet(ResultSet resultSet)
    {
        this.rs = resultSet;
    }

    @Override
    public Object getObject(String columnLabel)
    {
        try
        {
            return rs.getObject(columnLabel);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Object getObject(int columnIndex)
    {
        try
        {
            return rs.getObject(columnIndex);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public int getInt(String columnLabel)
    {
        try
        {
            return rs.getInt(columnLabel);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public int getInt(int columnIndex)
    {
        try
        {
            return rs.getInt(columnIndex);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String getString(String columnLabel)
    {
        try
        {
            return rs.getString(columnLabel);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String getString(int columnIndex)
    {
        try
        {
            return rs.getString(columnIndex);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public double getDouble(String columnLabel)
    {
        try
        {
            return rs.getDouble(columnLabel);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public double getDouble(int columnIndex)
    {
        try
        {
            return rs.getDouble(columnIndex);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public int getColumnCount()
    {
        try
        {
            ResultSetMetaData metaData = rs.getMetaData();
            return metaData.getColumnCount();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String getColumnLabel(int index)
    {
        try
        {
            ResultSetMetaData metaData = rs.getMetaData();
            return metaData.getColumnLabel(index);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Row getCurrentRow()
    {
        return this;
    }

    @Override
    public boolean next()
    {
        try
        {
            return rs.next();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}