package com.byx;

import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Properties;

/**
 * JDBC工具类
 * <p>使用前，需要在resources目录下创建db.properties文件，并写入数据库相关配置</p>
 * <p>配置如下：</p>
 * <p>jdbc.driver: 数据库驱动类名</p>
 * <p>jdbc.url: 连接字符串</p>
 * <p>jdbc.username: 用户名</p>
 * <p>jdbc.password: 密码</p>
 */
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

    /**
     * 查询
     * @param sql sql语句
     * @param resultSetMapper 将ResultSet处理成一个结果
     * @param params sql参数
     * @param <T> 结果类型
     * @return 结果，若出错则返回null
     */
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

    /**
     * 查询列表
     * @param sql sql语句
     * @param rowMapper 将一行ResultSet处理成一个结果
     * @param params sql参数
     * @param <T> 列表元素类型
     * @return 结果列表，若出错则返回null
     */
    public static <T> List<T> queryList(String sql, RowMapper<T> rowMapper, Object... params)
    {
        return query(sql, new ListResultSetMapper<>(rowMapper), params);
    }

    /**
     * 查询列表
     * @param sql sql语句
     * @param type 列表元素类型
     * @param params sql参数
     * @param <T> 列表元素类型
     * @return 结果列表，若出错则返回null
     */
    public static <T> List<T> queryList(String sql, Class<T> type, Object... params)
    {
        return query(sql, new ListResultSetMapper<>(new BeanRowMapper<>(type)), params);
    }

    /**
     * 查询单列
     * @param sql sql语句
     * @param params sql参数
     * @param <T> 结果类型
     * @return 结果，若出错则返回null
     */
    public static <T> T queryValue(String sql, Object... params)
    {
        return query(sql, new SingleColumnResultSetMapper<>(), params);
    }

    /**
     * 查询单行
     * @param sql sql语句
     * @param type 结果类型
     * @param params sql参数
     * @param <T> 结果类型
     * @return 结果，若出错则返回null
     */
    public static <T> T queryObject(String sql, Class<T> type, Object... params)
    {
        return query(sql, new SingleRowResultSetMapper<>(type), params);
    }

    /**
     * 更新操作
     * @param sql sql语句
     * @param params sql参数
     * @return 影响行数，若出错则返回-1
     */
    public static int update(String sql, Object... params)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

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
