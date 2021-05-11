package byx.util.jdbc.core;

import byx.util.jdbc.exception.DbException;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 使用ThreadLocal封装Connection以实现类型安全
 *
 * @author byx
 */
public class ConnectionHolder {
    private final DataSource dataSource;
    private final ThreadLocal<Connection> holder = new ThreadLocal<>();

    public ConnectionHolder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection get() {
        try {
            Connection conn = holder.get();
            if (conn == null) {
                conn = dataSource.getConnection();
                holder.set(conn);
            }
            return conn;
        } catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }
    }

    public void close() {
        try {
            Connection conn = holder.get();
            if (conn != null) {
                conn.close();
                holder.remove();
            }
        } catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }
    }

    public boolean holding() {
        return holder.get() != null;
    }
}
