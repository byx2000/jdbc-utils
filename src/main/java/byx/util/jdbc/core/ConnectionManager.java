package byx.util.jdbc.core;

import byx.util.jdbc.exception.DbException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 连接管理器
 * 封装了线程安全的数据库连接
 *
 * @author byx
 */
public final class ConnectionManager {
    private final DataSource dataSource;
    private final ThreadLocal<Connection> connHolder = new ThreadLocal<>();

    public ConnectionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() {
        try {
            Connection conn = connHolder.get();
            if (conn == null) {
                conn = dataSource.getConnection();
                connHolder.set(conn);
            }
            return conn;
        } catch (SQLException e) {
            throw new DbException("An error occurred while creating a database connection.", e);
        }
    }

    public void close(Connection conn, Statement stmt) {
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
            throw new DbException("An error occurred while starting transaction.", e);
        }
    }

    public void commit() {
        Connection conn = connHolder.get();
        if (conn != null) {
            try {
                conn.commit();
                conn.close();
                connHolder.remove();
            } catch (SQLException e) {
                throw new DbException("An error occurred while committing transaction.", e);
            }
        }
    }

    public void rollback() {
        Connection conn = connHolder.get();
        if (conn != null) {
            try {
                conn.rollback();
                conn.close();
                connHolder.remove();
            } catch (SQLException e) {
                throw new DbException("An error occurred while committing transaction.", e);
            }
        }
    }

    public boolean inTransaction() {
        Connection conn = connHolder.get();
        try {
            return conn != null && !conn.getAutoCommit();
        } catch (SQLException e) {
            throw new DbException("An error occurred while getting auto commit.", e);
        }
    }
}
