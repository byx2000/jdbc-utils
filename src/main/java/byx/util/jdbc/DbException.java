package byx.util.jdbc;

/**
 * 数据库异常基类
 */
public class DbException extends RuntimeException {
    public DbException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
