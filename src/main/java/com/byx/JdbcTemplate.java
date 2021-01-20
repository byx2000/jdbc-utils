package com.byx;

import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class JdbcTemplate
{
    private static String url;
    private static String username;
    private static String password;

    static
    {
        try
        {
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties");
            Properties properties = new Properties();
            properties.load(in);
            String driver = properties.getProperty("jdbc.driver");
            url = properties.getProperty("jdbc.url");
            username = properties.getProperty("jdbc.username");
            password = properties.getProperty("jdbc.password");
            Class.forName(driver);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(url, username, password);
    }

    private static void close(ResultSet rs, Statement stmt, Connection conn)
    {
        if (rs != null)
        {
            try
            {
                rs.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

        if (stmt != null)
        {
            try
            {
                stmt.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

        if (conn != null)
        {
            try
            {
                conn.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static void close(Statement stmt, Connection conn)
    {
        if (stmt != null)
        {
            try
            {
                stmt.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

        if (conn != null)
        {
            try
            {
                conn.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static <T> T query(String sql, ResultSetMapper<T> resultSetMapper, Object... params)
    {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        Connection conn = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; ++i)
            {
                stmt.setObject(i + 1, params[i]);
            }

            rs = stmt.executeQuery();
            return resultSetMapper.map(rs);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            close(rs, stmt, conn);
        }
    }

    public static <T> List<T> queryList(String sql, RowMapper<T> rowMapper, Object... params)
    {
        return query(sql, new ListResultSetMapper<>(rowMapper), params);
    }

    public static <T> T queryValue(String sql, Object... params)
    {
        return query(sql, new SingleColumnResultSetMapper<>(), params);
    }

    public static <T> T queryObject(String sql, Class<T> type, Object... params)
    {
        return query(sql, new SingleRowResultSetMapper<>(type), params);
    }

    public static int update(String sql, Object... params)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            // 设置sql参数
            for (int i = 0; i < params.length; ++i)
            {
                stmt.setObject(i + 1, params[i]);
            }

            return stmt.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
        finally
        {
            close(stmt, conn);
        }
    }
}
