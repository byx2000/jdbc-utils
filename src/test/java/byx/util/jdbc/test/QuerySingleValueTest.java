package byx.util.jdbc.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuerySingleValueTest extends BaseTest {
    @Test
    public void test1() {
        Integer count = jdbcUtils.querySingleValue("SELECT COUNT(*) FROM users");

        assertNotNull(count);
        assertEquals(5, count);
    }

    @Test
    public void test2() {
        Integer count = jdbcUtils.querySingleValue("SELECT COUNT(*) FROM users WHERE password = ?", "456");

        assertNotNull(count);
        assertEquals(2, count);
    }

    @Test
    public void test3() {
        Integer count = jdbcUtils.querySingleValue("SELECT COUNT(*) FROM users WHERE password = ?", "10086");

        assertNotNull(count);
        assertEquals(0, count);
    }

    @Test
    public void test4() {
        assertThrows(RuntimeException.class, () -> jdbcUtils.querySingleValue("SELECT COUNT(*) FRAME users"));
    }

    @Test
    public void test5() {
        assertThrows(RuntimeException.class, () -> jdbcUtils.querySingleValue("SELECT COUNT(*) FROM users", 3));
    }

    @Test
    public void test6() {
        String username = jdbcUtils.querySingleValue("SELECT username FROM users WHERE id = 1");

        assertEquals("aaa", username);
    }

    @Test
    public void test7() {
        String username = jdbcUtils.querySingleValue("SELECT username FROM users WHERE id = 1001");

        assertNull(username);
    }

    @Test
    public void test8() {
        int count = jdbcUtils.querySingleValue("SELECT COUNT(*) FROM users");

        assertEquals(5, count);
    }
}
