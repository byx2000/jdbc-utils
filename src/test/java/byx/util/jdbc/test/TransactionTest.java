package byx.util.jdbc.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest extends BaseTest {
    @Test
    public void test1() {
        try {
            assertFalse(jdbcUtils.inTransaction());
            jdbcUtils.startTransaction();
            jdbcUtils.update("update A set value = value - 10");
            assertTrue(jdbcUtils.inTransaction());
            int a = 1 / 0;
            jdbcUtils.update("update B set value = value + 10");
            jdbcUtils.commit();
        } catch (Exception e) {
            jdbcUtils.rollback();
            assertFalse(jdbcUtils.inTransaction());
        }

        assertEquals(100, (int) jdbcUtils.querySingleValue("select value from A"));
        assertEquals(0, (int) jdbcUtils.querySingleValue("select value from B"));

        try {
            assertFalse(jdbcUtils.inTransaction());
            jdbcUtils.startTransaction();
            assertTrue(jdbcUtils.inTransaction());
            jdbcUtils.update("update A set value = value - 10");
            jdbcUtils.update("update B set value = value + 10");
            assertTrue(jdbcUtils.inTransaction());
            jdbcUtils.commit();
            assertFalse(jdbcUtils.inTransaction());
        } catch (Exception e) {
            jdbcUtils.rollback();
        }

        assertEquals(90, (int) jdbcUtils.querySingleValue("select value from A"));
        assertEquals(10, (int) jdbcUtils.querySingleValue("select value from B"));

        try {
            jdbcUtils.startTransaction();
            jdbcUtils.update("update A set value = 100");
            int a = 1 / 0;
            jdbcUtils.update("update B set value = 0");
            jdbcUtils.commit();
        } catch (Exception e) {
            jdbcUtils.rollback();
        }

        assertEquals(90, (int) jdbcUtils.querySingleValue("select value from A"));
        assertEquals(10, (int) jdbcUtils.querySingleValue("select value from B"));

        try {
            jdbcUtils.startTransaction();
            jdbcUtils.update("update A set value = 100");
            jdbcUtils.update("update B set value = 0");
            jdbcUtils.commit();
        } catch (Exception e) {
            jdbcUtils.rollback();
        }

        assertEquals(100, (int) jdbcUtils.querySingleValue("select value from A"));
        assertEquals(0, (int) jdbcUtils.querySingleValue("select value from B"));
    }

    @Test
    public void test2() {
        jdbcUtils.startTransaction();
        jdbcUtils.update("update A set value = value - 10");
        jdbcUtils.startTransaction();
        jdbcUtils.querySingleValue("select value from A");
        jdbcUtils.querySingleValue("select value from B");
        jdbcUtils.commit();
        jdbcUtils.update("update B set value = value + 10");
        jdbcUtils.commit();

        assertEquals(90, (int) jdbcUtils.querySingleValue("select value from A"));
        assertEquals(10, (int) jdbcUtils.querySingleValue("select value from B"));

        jdbcUtils.update("update A set value = 100");
        jdbcUtils.update("update B set value = 0");
        assertEquals(100, (int) jdbcUtils.querySingleValue("select value from A"));
        assertEquals(0, (int) jdbcUtils.querySingleValue("select value from B"));
    }
}