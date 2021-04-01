package byx.util.jdbc.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionTest extends BaseTest {
    @Test
    public void test() {
        try {
            jdbcUtils.startTransaction();
            jdbcUtils.update("update A set value = value - 10");
            int a = 1 / 0;
            jdbcUtils.update("update B set value = value + 10");
            jdbcUtils.commit();
        } catch (Exception e) {
            jdbcUtils.rollback();
        }

        assertEquals(100, jdbcUtils.querySingleValue("select value from A", Integer.class));
        assertEquals(0, jdbcUtils.querySingleValue("select value from B", Integer.class));

        try {
            jdbcUtils.startTransaction();
            jdbcUtils.update("update A set value = value - 10");
            jdbcUtils.update("update B set value = value + 10");
            jdbcUtils.commit();
        } catch (Exception e) {
            jdbcUtils.rollback();
        }

        assertEquals(90, jdbcUtils.querySingleValue("select value from A", Integer.class));
        assertEquals(10, jdbcUtils.querySingleValue("select value from B", Integer.class));

        try {
            jdbcUtils.startTransaction();
            jdbcUtils.update("update A set value = 100");
            int a = 1 / 0;
            jdbcUtils.update("update B set value = 0");
            jdbcUtils.commit();
        } catch (Exception e) {
            jdbcUtils.rollback();
        }

        assertEquals(90, jdbcUtils.querySingleValue("select value from A", Integer.class));
        assertEquals(10, jdbcUtils.querySingleValue("select value from B", Integer.class));

        try {
            jdbcUtils.startTransaction();
            jdbcUtils.update("update A set value = 100");
            jdbcUtils.update("update B set value = 0");
            jdbcUtils.commit();
        } catch (Exception e) {
            jdbcUtils.rollback();
        }

        assertEquals(100, jdbcUtils.querySingleValue("select value from A", Integer.class));
        assertEquals(0, jdbcUtils.querySingleValue("select value from B", Integer.class));
    }
}
