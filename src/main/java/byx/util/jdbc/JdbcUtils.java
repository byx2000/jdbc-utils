package byx.util.jdbc;

import byx.util.jdbc.core.*;

import java.io.IOException;
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
 *
 * @author byx
 */
public class JdbcUtils {
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    /**
     * 用于在一个事务内共享数据库连接
     */
    private static final ThreadLocal<Connection> connHolder = new ThreadLocal<>();

    /**
     * 判断当前是否在事务中
     */
    private static boolean inTransaction() {
        return connHolder.get() != null;
    }

    static {
        try {
            // 读取resources目录下的db.properties文件
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties");
            Properties properties = new Properties();
            properties.load(in);

            // 读取驱动类名、连接字符串、用户名和密码
            String driver = properties.getProperty("jdbc.driver");
            URL = properties.getProperty("jdbc.url");
            USERNAME = properties.getProperty("jdbc.username");
            PASSWORD = properties.getProperty("jdbc.password");

            // 加载驱动
            Class.forName(driver);
        } catch (NullPointerException | IOException e) {
            throw new RuntimeException("找不到db.properties文件，请在resources目录下创建db.properties文件，并写入数据库配置", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("找不到数据库驱动类", e);
        }
    }

    /**
     * 创建新连接
     */
    private static Connection getNewConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取连接
     *
     * @return 连接
     */
    private static Connection getConnection() {
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
    private static void close(ResultSet rs, Statement stmt, Connection conn) {
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
    private static void close(Statement stmt, Connection conn) {
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
    private static PreparedStatement createPreparedStatement(Connection conn, String sql, Object... params) throws SQLException {
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
    public static <T> T query(String sql, RecordMapper<T> recordMapper, Object... params) {
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
    public static <T> List<T> queryList(String sql, RowMapper<T> rowMapper, Object... params) {
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
    public static <T> List<T> queryList(String sql, Class<T> type, Object... params) {
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
    public static <T> T querySingleValue(String sql, Class<T> type, Object... params) {
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
    public static <T> T querySingleRow(String sql, RowMapper<T> rowMapper, Object... params) {
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
    public static <T> T querySingleRow(String sql, Class<T> type, Object... params) {
        return querySingleRow(sql, new BeanRowMapper<>(type), params);
    }

    /**
     * 更新数据库，返回影响行数
     *
     * @param sql    sql语句
     * @param params sql参数
     * @return 成功则返回影响行数，失败则抛出RuntimeException
     */
    public static int update(String sql, Object... params) {
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
    public static void startTransaction() {
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
    public static void commit() {
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
    public static void rollback() {
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
