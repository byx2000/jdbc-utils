package byx.util.jdbc.test;

import byx.util.jdbc.JdbcUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

public class TransactionTest {
    @Test
    public void test() throws SQLException {
        try {
            JdbcUtils.startTransaction();
            JdbcUtils.update("update A set value = value - 10");
            int a = 1 / 0;
            JdbcUtils.update("update B set value = value + 10");
            JdbcUtils.commit();
        } catch (Exception e) {
            JdbcUtils.rollback();
        }

        assertEquals(100, JdbcUtils.querySingleValue("select value from A", Integer.class));
        assertEquals(0, JdbcUtils.querySingleValue("select value from B", Integer.class));

        try {
            JdbcUtils.startTransaction();
            JdbcUtils.update("update A set value = value - 10");
            JdbcUtils.update("update B set value = value + 10");
            JdbcUtils.commit();
        } catch (Exception e) {
            JdbcUtils.rollback();
        }

        assertEquals(90, JdbcUtils.querySingleValue("select value from A", Integer.class));
        assertEquals(10, JdbcUtils.querySingleValue("select value from B", Integer.class));

        try {
            JdbcUtils.startTransaction();
            JdbcUtils.update("update A set value = 100");
            int a = 1 / 0;
            JdbcUtils.update("update B set value = 0");
            JdbcUtils.commit();
        } catch (Exception e) {
            JdbcUtils.rollback();
        }

        assertEquals(90, JdbcUtils.querySingleValue("select value from A", Integer.class));
        assertEquals(10, JdbcUtils.querySingleValue("select value from B", Integer.class));

        try {
            JdbcUtils.startTransaction();
            JdbcUtils.update("update A set value = 100");
            JdbcUtils.update("update B set value = 0");
            JdbcUtils.commit();
        } catch (Exception e) {
            JdbcUtils.rollback();
        }

        assertEquals(100, JdbcUtils.querySingleValue("select value from A", Integer.class));
        assertEquals(0, JdbcUtils.querySingleValue("select value from B", Integer.class));
    }
}
