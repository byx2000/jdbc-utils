package com.byx.jdbc;

import com.byx.jdbc.mapper.*;

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
    /**
     * 连接字符串
     */
    private static final String url;

    /**
     * 用户名
     */
    private static final String username;

    /**
     * 密码
     */
    private static final String password;

    static
    {
        try
        {
            // 读取resources目录下的db.properties文件
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties");
            Properties properties = new Properties();
            properties.load(in);

            // 读取驱动类名、连接字符串、用户名和密码
            String driver = properties.getProperty("jdbc.driver");
            url = properties.getProperty("jdbc.url");
            username = properties.getProperty("jdbc.username");
            password = properties.getProperty("jdbc.password");

            // 加载驱动
            Class.forName(driver);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取连接
     * @return 连接
     * @throws SQLException 异常
     */
    public static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * 释放资源
     * @param rs 结果集
     * @param stmt 语句
     * @param conn 连接
     */
    public static void close(ResultSet rs, Statement stmt, Connection conn)
    {
        if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
        if (stmt != null) try { stmt.close(); } catch (SQLException ignored) {}
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
    }

    /**
     * 释放资源
     * @param stmt 语句
     * @param conn 连接
     */
    public static void close(Statement stmt, Connection conn)
    {
        if (stmt != null) try { stmt.close(); } catch (SQLException ignored) {}
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
    }

    /**
     * 创建语句
     * @param conn 连接
     * @param sql sql语句
     * @param params sql参数
     * @return PreparedStatement对象
     * @throws SQLException 异常
     */
    public static PreparedStatement createPreparedStatement(Connection conn, String sql, Object... params) throws SQLException
    {
        PreparedStatement stmt = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; ++i)
        {
            stmt.setObject(i + 1, params[i]);
        }
        return stmt;
    }

    /**
     * 查询
     * @param sql sql语句
     * @param resultSetMapper 结果集转换器
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
            stmt = createPreparedStatement(conn, sql, params);
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
     * @param rowMapper 行转换器
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
     * 查询单个值
     * @param sql sql语句
     * @param type 结果类型
     * @param params sql参数
     * @param <T> 结果类型
     * @return 结果，若出错则返回null
     */
    public static <T> T querySingleValue(String sql, Class<T> type, Object... params)
    {
        return query(sql, new SingleRowResultSetMapper<>(new SingleColumnRowMapper<>(type)), params);
    }

    /**
     * 查询单行
     * @param sql sql语句
     * @param rowMapper 行转换器
     * @param params sql参数
     * @param <T> 结果类型
     * @return 结果，若出错则返回null
     */
    public static <T> T querySingleRow(String sql, RowMapper<T> rowMapper, Object... params)
    {
        return query(sql, new SingleRowResultSetMapper<>(rowMapper), params);
    }

    /**
     * 查询单行
     * @param sql sql语句
     * @param type JavaBean类型
     * @param params sql参数
     * @param <T> JavaBean类型
     * @return 结果JavaBean
     */
    public static <T> T querySingleRow(String sql, Class<T> type, Object... params)
    {
        return querySingleRow(sql, new BeanRowMapper<>(type), params);
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
            stmt = createPreparedStatement(conn, sql, params);
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
