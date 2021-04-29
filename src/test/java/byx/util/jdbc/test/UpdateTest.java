package byx.util.jdbc.test;

import byx.util.jdbc.core.BeanRowMapper;
import byx.util.jdbc.test.domain.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateTest extends BaseTest {
    @Test
    public void test1() {
        int count = jdbcUtils.update("INSERT INTO users(username, password) VALUES(?, ?)", "byx", "123456");
        assertEquals(1, count);

        List<User> users = jdbcUtils.queryList("SELECT * FROM users", new BeanRowMapper<>(User.class));
        assertNotNull(users);
        assertEquals(6, users.size());

        count = jdbcUtils.querySingleValue("SELECT COUNT(*) FROM users");
        assertEquals(6, count);

        User user = jdbcUtils.querySingleRow("SELECT * FROM users WHERE username = ? AND password = ?", User.class, "byx", "123456");
        assertNotNull(user);
        assertEquals("byx", user.getUsername());
        assertEquals("123456", user.getPassword());

        count = jdbcUtils.update("DELETE FROM users WHERE username = 'byx' AND password = '123456'");
        assertEquals(1, count);

        users = jdbcUtils.queryList("SELECT * FROM users", User.class);
        assertNotNull(users);
        assertEquals(5, users.size());

        count = jdbcUtils.querySingleValue("SELECT COUNT(*) FROM users");
        assertEquals(5, count);
    }

    @Test
    public void test2() {
        assertThrows(RuntimeException.class, () -> jdbcUtils.update("INSECT INTO users(username, password) VALUES(?, ?)"));
    }

    @Test
    public void test3() {
        assertThrows(RuntimeException.class, () -> jdbcUtils.update("INSERT INTO users(username, password) VALUES('byx', '123456')", "byx", "123456"));
    }
}
