package byx.util.jdbc;

import byx.util.jdbc.core.*;
import byx.util.jdbc.exception.DbException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Properties;

/**
 * JDBC工具类
 *
 * @author byx
 */
public class JdbcUtils {
    private final String url;
    private final String username;
    private final String password;

    /**
     * 用于在一个事务内共享数据库连接
     */
    private final ThreadLocal<Connection> connHolder = new ThreadLocal<>();

    /**
     * 创建JdbcUtils
     * @param driver 数据库驱动类名
     * @param url 连接字符串
     * @param username 用户名
     * @param password 密码
     */
    public JdbcUtils(String driver, String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new DbException("Driver class not found: " + driver, e);
        }
    }

    /**
     * 创建JdbcUtils
     * @param propertiesFile properties文件路径
     */
    public JdbcUtils(String propertiesFile) {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFile);
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            throw new DbException("Cannot read properties file: " + propertiesFile, e);
        }

        String driver = properties.getProperty("jdbc.driver");
        url = properties.getProperty("jdbc.url");
        username = properties.getProperty("jdbc.username");
        password = properties.getProperty("jdbc.password");
        if (driver == null || url == null || username == null || password == null) {
            throw new DbException("Properties file contains these key: jdbc.driver, jdbc.url, jdbc.username, dbc.password");
        }


        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new DbException("Driver class not found: " + driver, e);
        }
    }

    /**
     * 判断当前是否在事务中
     */
    private boolean inTransaction() {
        return connHolder.get() != null;
    }

    /**
     * 创建新连接
     */
    private Connection getNewConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取连接
     *
     * @return 连接
     */
    private Connection getConnection() {
        Connection conn = connHolder.get();
        if (conn == null) {
            return getNewConnection();
        } else {
            return conn;
        }
    }

    /**
     * 释放资源
     *
     * @param rs   结果集
     * @param stmt 语句
     * @param conn 连接
     */
    private void close(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException ignored) { }
        }
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException ignored) { }
        }
        if (!inTransaction() && conn != null) {
            try { conn.close(); } catch (SQLException ignored) { }
        }
    }

    /**
     * 释放资源
     *
     * @param stmt 语句
     * @param conn 连接
     */
    private void close(Statement stmt, Connection conn) {
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException ignored) { }
        }
        if (!inTransaction() && conn != null) {
            try { conn.close(); } catch (SQLException ignored) { }
        }
    }

    /**
     * 创建语句
     *
     * @param conn   连接
     * @param sql    sql语句
     * @param params sql参数
     * @return 创建的PreparedStatement对象
     * @throws SQLException 来自JDBC的异常
     */
    private PreparedStatement createPreparedStatement(Connection conn, String sql, Object... params) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; ++i) {
            stmt.setObject(i + 1, params[i]);
        }
        return stmt;
    }

    /**
     * 查询数据库并转换结果集。
     * 用户可自定义结果集转换器。
     * 用户也可使用预定义的结果集转换器。
     *
     * @param sql          sql语句
     * @param recordMapper 结果集转换器
     * @param params       sql参数
     * @param <T>          resultSetMapper返回的结果类型
     * @return 成功则返回转换结果，失败则抛出RuntimeException，结果为空则返回空列表
     * @see RecordMapper
     * @see ListRecordMapper
     * @see SingleRowRecordMapper
     */
    public <T> T query(String sql, RecordMapper<T> recordMapper, Object... params) {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = getConnection();
            stmt = createPreparedStatement(conn, sql, params);
            rs = stmt.executeQuery();
            return recordMapper.map(new RecordAdapterForResultSet(rs));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            close(rs, stmt, conn);
        }
    }

    /**
     * 查询数据库，对结果集的每一行进行转换，然后将所有行封装成列表。
     * 用户可自定义行转换器。
     * 用户也可使用预定义的行转换器。
     *
     * @param sql       sql语句
     * @param rowMapper 行转换器
     * @param params    sql参数
     * @param <T>       rowMapper返回的结果类型
     * @return 成功则返回结果列表，失败则抛出RuntimeException，结果为空则返回空列表
     * @see RowMapper
     * @see BeanRowMapper
     * @see MapRowMapper
     * @see SingleColumnRowMapper
     */
    public <T> List<T> queryList(String sql, RowMapper<T> rowMapper, Object... params) {
        return query(sql, new ListRecordMapper<>(rowMapper), params);
    }

    /**
     * 查询数据库，将结果集的每一行转换成JavaBean，然后将所有行封装成列表。
     *
     * @param sql    sql语句
     * @param type   JavaBean类型
     * @param params sql参数
     * @param <T>    JavaBean类型
     * @return 成功则返回结果列表，失败则抛出RuntimeException，结果为空则返回空列表
     */
    public <T> List<T> queryList(String sql, Class<T> type, Object... params) {
        return query(sql, new ListRecordMapper<>(new BeanRowMapper<>(type)), params);
    }

    /**
     * 查询数据库，返回结果集中的单个值。
     * 如果结果集中有多个值，则只返回第一行第一列的值。
     *
     * @param sql    sql语句
     * @param type   结果类型
     * @param params sql参数
     * @param <T>    结果类型
     * @return 成功则返回结果值，失败则抛出RuntimeException，结果为空则返回null
     */
    public <T> T querySingleValue(String sql, Class<T> type, Object... params) {
        return query(sql, new SingleRowRecordMapper<>(new SingleColumnRowMapper<>(type)), params);
    }

    /**
     * 查询数据库，返回结果集中的单行数据。
     * 如果结果集中有多行数据，则只返回第一行数据。
     * 用户可自定义行转换器。
     * 用户也可使用预定义的行转换器。
     *
     * @param sql       sql语句
     * @param rowMapper 行转换器
     * @param params    sql参数
     * @param <T>       rowMapper返回的结果类型
     * @return 成功则返回结果，失败则抛出RuntimeException，结果为空则返回null
     * @see RowMapper
     * @see BeanRowMapper
     * @see MapRowMapper
     * @see SingleColumnRowMapper
     */
    public <T> T querySingleRow(String sql, RowMapper<T> rowMapper, Object... params) {
        return query(sql, new SingleRowRecordMapper<>(rowMapper), params);
    }

    /**
     * 查询数据库，将结果集中的单行数据转换成JavaBean。
     *
     * @param sql    sql语句
     * @param type   JavaBean类型
     * @param params sql参数
     * @param <T>    JavaBean类型
     * @return 成功则返回结果，失败则抛出RuntimeException，结果为空则返回null
     */
    public <T> T querySingleRow(String sql, Class<T> type, Object... params) {
        return querySingleRow(sql, new BeanRowMapper<>(type), params);
    }

    /**
     * 更新数据库，返回影响行数
     *
     * @param sql    sql语句
     * @param params sql参数
     * @return 成功则返回影响行数，失败则抛出RuntimeException
     */
    public int update(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = createPreparedStatement(conn, sql, params);
            return stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            close(stmt, conn);
        }
    }

    /**
     * 开启事务
     */
    public void startTransaction() {
        try {
            Connection conn = getNewConnection();
            connHolder.set(conn);
            conn.setAutoCommit(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 提交事务
     */
    public void commit() {
        try {
            Connection conn = connHolder.get();
            conn.commit();
            conn.close();
            connHolder.remove();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 回滚事务
     */
    public void rollback() {
        try {
            Connection conn = connHolder.get();
            conn.rollback();
            conn.close();
            connHolder.remove();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
