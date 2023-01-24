package byx.util.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 线程安全的连接管理器
 */
public final class ConnectionManager {
    private final DataSource dataSource;
    private final ThreadLocal<Connection> connHolder = new ThreadLocal<>();

    public ConnectionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 获取连接
     */
    public Connection getConnection() {
        try {
            Connection conn = connHolder.get();
            if (conn == null) {
                conn = dataSource.getConnection();
                connHolder.set(conn);
            }
            return conn;
        } catch (SQLException e) {
            throw new DbException("error when get database connection", e);
        }
    }

    /**
     * 资源释放
     */
    public void close(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException ignored) {}
        }
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException ignored) {}
        }
        if (conn != null) {
            try {
                if (conn.getAutoCommit()) {
                    conn.close();
                    connHolder.remove();
                }
            } catch (SQLException ignored) {}
        }
    }

    /**
     * 在当前连接上开启事务
     */
    public void startTransaction() {
        try {
            Connection conn = connHolder.get();
            if (conn != null) {
                conn.close();
                connHolder.remove();
            }
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            connHolder.set(conn);
        } catch (SQLException e) {
            throw new DbException("error when start transaction", e);
        }
    }

    /**
     * 在当前连接上提交事务
     */
    public void commit() {
        Connection conn = connHolder.get();
        if (conn != null) {
            try {
                conn.commit();
                conn.close();
                connHolder.remove();
            } catch (SQLException e) {
                throw new DbException("error when commit transaction", e);
            }
        }
    }

    /**
     * 在当前连接上回滚事务
     */
    public void rollback() {
        Connection conn = connHolder.get();
        if (conn != null) {
            try {
                conn.rollback();
                conn.close();
                connHolder.remove();
            } catch (SQLException e) {
                throw new DbException("error when rollback transaction", e);
            }
        }
    }

    /**
     * 判断当前连接是否在事务中
     */
    public boolean inTransaction() {
        Connection conn = connHolder.get();
        try {
            return conn != null && !conn.getAutoCommit();
        } catch (SQLException e) {
            throw new DbException("error when get transaction status", e);
        }
    }
}
