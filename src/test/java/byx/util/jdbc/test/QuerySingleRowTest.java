package byx.util.jdbc.test;

import byx.util.jdbc.JdbcUtils;
import byx.util.jdbc.core.BeanRowMapper;
import byx.util.jdbc.test.domain.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuerySingleRowTest {
    private final JdbcUtils jdbcUtils = new JdbcUtils("db.properties");
    
    @Test
    public void test1() {
        User user = jdbcUtils.querySingleRow("SELECT * FROM users WHERE id = 2", new BeanRowMapper<>(User.class));

        assertNotNull(user);
        assertEquals(2, user.getId());
        assertEquals("bbb", user.getUsername());
        assertEquals("456", user.getPassword());
    }

    @Test
    public void test2() {
        User user = jdbcUtils.querySingleRow("SELECT * FROM users WHERE id = ?", new BeanRowMapper<>(User.class), "2");

        assertNotNull(user);
        assertEquals(2, user.getId());
        assertEquals("bbb", user.getUsername());
        assertEquals("456", user.getPassword());
    }

    @Test
    public void test3() {
        String username = jdbcUtils.querySingleRow("SELECT * FROM users WHERE id = 2", row -> row.getString("username"));

        assertNotNull(username);
        assertEquals("bbb", username);
    }

    @Test
    public void test4() {
        User user = jdbcUtils.querySingleRow("SELECT * FROM users WHERE id = 1001", new BeanRowMapper<>(User.class));
        assertNull(user);
    }

    @Test
    public void test5() {
        assertThrows(RuntimeException.class, () -> jdbcUtils.querySingleRow("SELECT * FROM users WEAR id = 2", new BeanRowMapper<>(User.class)));
    }

    @Test
    public void test6() {
        assertThrows(RuntimeException.class, () -> jdbcUtils.querySingleRow("SELECT * FROM users WEAR id = 2", new BeanRowMapper<>(User.class), 2));
    }

    @Test
    public void test7() {
        User user = jdbcUtils.querySingleRow("SELECT * FROM users WHERE id = 2", User.class);

        assertNotNull(user);
        assertEquals(2, user.getId());
        assertEquals("bbb", user.getUsername());
        assertEquals("456", user.getPassword());
    }

    @Test
    public void test8() {
        User user = jdbcUtils.querySingleRow("SELECT * FROM users WHERE id = ?", User.class, "2");

        assertNotNull(user);
        assertEquals(2, user.getId());
        assertEquals("bbb", user.getUsername());
        assertEquals("456", user.getPassword());
    }

    @Test
    public void test9() {
        User user = jdbcUtils.querySingleRow("SELECT * FROM users WHERE id = 1001", User.class);

        assertNull(user);
    }

    @Test
    public void test10() {
        assertThrows(RuntimeException.class, () -> jdbcUtils.querySingleRow("SELECT * FROM users WEAR id = 2", User.class));
    }

    @Test
    public void test11() {
        assertThrows(RuntimeException.class, () -> jdbcUtils.querySingleRow("SELECT * FROM users WEAR id = 2", User.class, 2));
    }
}
