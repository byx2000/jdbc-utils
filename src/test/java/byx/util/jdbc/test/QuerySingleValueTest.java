package byx.util.jdbc.test;

import byx.util.jdbc.JdbcUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuerySingleValueTest {
    @Test
    public void test1() {
        Integer count = JdbcUtils.querySingleValue("SELECT COUNT(*) FROM users", Integer.class);

        assertNotNull(count);
        assertEquals(5, count);
    }

    @Test
    public void test2() {
        Integer count = JdbcUtils.querySingleValue("SELECT COUNT(*) FROM users WHERE password = ?", Integer.class, "456");

        assertNotNull(count);
        assertEquals(2, count);
    }

    @Test
    public void test3() {
        Integer count = JdbcUtils.querySingleValue("SELECT COUNT(*) FROM users WHERE password = ?", Integer.class, "10086");

        assertNotNull(count);
        assertEquals(0, count);
    }

    @Test
    public void test4() {
        assertThrows(RuntimeException.class, () -> JdbcUtils.querySingleValue("SELECT COUNT(*) FRAME users", Integer.class));
    }

    @Test
    public void test5() {
        assertThrows(RuntimeException.class, () -> JdbcUtils.querySingleValue("SELECT COUNT(*) FROM users", Integer.class, 3));
    }

    @Test
    public void test6() {
        String username = JdbcUtils.querySingleValue("SELECT username FROM users WHERE id = 1", String.class);

        assertEquals("aaa", username);
    }

    @Test
    public void test7() {
        String username = JdbcUtils.querySingleValue("SELECT username FROM users WHERE id = 1001", String.class);

        assertNull(username);
    }
}
