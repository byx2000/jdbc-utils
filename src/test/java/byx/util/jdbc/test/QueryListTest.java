package byx.util.jdbc.test;

import byx.util.jdbc.JdbcUtils;
import byx.util.jdbc.core.BeanRowMapper;
import byx.util.jdbc.core.Row;
import byx.util.jdbc.test.domain.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QueryListTest {
    @Test
    public void test1() {
        List<User> users = JdbcUtils.queryList("SELECT * FROM users", new BeanRowMapper<>(User.class));

        assertNotNull(users);
        assertEquals(5, users.size());
    }

    @Test
    public void test2() {
        List<User> users = JdbcUtils.query("SELECT * FROM users", record -> {
            List<User> us = new ArrayList<>();
            while (record.next()) {
                Row row = record.getCurrentRow();
                User u = new User();
                u.setId(row.getInt("id"));
                u.setUsername(row.getString("username"));
                u.setPassword(row.getString("password"));
                us.add(u);
            }
            return us;
        });

        assertNotNull(users);
        assertEquals(5, users.size());
    }

    @Test
    public void test3() {
        List<User> users = JdbcUtils.queryList("SELECT * FROM users", row -> {
            User u = new User();
            u.setId(row.getInt("id"));
            u.setUsername(row.getString("username"));
            u.setPassword(row.getString("password"));
            return u;
        });

        assertNotNull(users);
        assertEquals(5, users.size());
    }

    @Test
    public void test4() {
        List<User> users = JdbcUtils.queryList("SELECT * FROM users WHERE password = ?", new BeanRowMapper<>(User.class), "456");

        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    public void test5() {
        List<User> users = JdbcUtils.queryList("SELECT * FROM users WHERE password = 10086", new BeanRowMapper<>(User.class));

        assertNotNull(users);
        assertEquals(0, users.size());
    }

    @Test
    public void test6() {
        List<String> usernames = JdbcUtils.queryList("SELECT * FROM users", row -> row.getString("username"));

        assertNotNull(usernames);
        assertEquals(5, usernames.size());
    }

    @Test()
    public void test7() {
        assertThrows(RuntimeException.class, () -> JdbcUtils.queryList("SELECT * FROM users WEAR id = 1", new BeanRowMapper<>(User.class)));
    }

    @Test
    public void test8() {
        assertThrows(RuntimeException.class, () -> JdbcUtils.queryList("SELECT * FROM users", new BeanRowMapper<>(User.class), "aaa"));
    }

    @Test
    public void test9() {
        List<User> users = JdbcUtils.queryList("SELECT * FROM users", User.class);

        assertNotNull(users);
        assertEquals(5, users.size());
    }

    @Test
    public void test10() {
        List<User> users = JdbcUtils.queryList("SELECT * FROM users WHERE password = ?", User.class, "456");

        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    public void test11() {
        List<User> users = JdbcUtils.queryList("SELECT * FROM users WHERE password = 10086", User.class);

        assertNotNull(users);
        assertEquals(0, users.size());
    }

    @Test
    public void test12() {
        assertThrows(RuntimeException.class, () -> JdbcUtils.queryList("SELECT * FROM", User.class));
    }

    @Test
    public void test13() {
        assertThrows(RuntimeException.class, () -> JdbcUtils.queryList("SELECT * FROM users", User.class, "aaa", "123"));
    }
}
