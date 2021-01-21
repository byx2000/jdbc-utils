package com.byx.jdbc.test;

import com.byx.jdbc.mapper.BeanRowMapper;
import com.byx.jdbc.JdbcTemplate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateTest
{
    @Test
    public void test1()
    {
        int count = JdbcTemplate.update("INSERT INTO users(username, password) VALUES(?, ?)", "byx", "123456");
        assertEquals(1, count);

        List<User> users = JdbcTemplate.queryList("SELECT * FROM users", new BeanRowMapper<>(User.class));
        assertNotNull(users);
        assertEquals(6, users.size());

        count = JdbcTemplate.queryValue("SELECT COUNT(*) FROM users");
        assertEquals(6, count);

        User user = JdbcTemplate.queryObject("SELECT * FROM users WHERE username = ? AND password = ?", User.class, "byx", "123456");
        assertNotNull(user);
        assertEquals("byx", user.getUsername());
        assertEquals("123456", user.getPassword());

        count = JdbcTemplate.update("DELETE FROM users WHERE username = 'byx' AND password = '123456'");
        assertEquals(1, count);

        users = JdbcTemplate.queryList("SELECT * FROM users", new BeanRowMapper<>(User.class));
        assertNotNull(users);
        assertEquals(5, users.size());

        count = JdbcTemplate.queryValue("SELECT COUNT(*) FROM users");
        assertEquals(5, count);
    }

    @Test
    public void test2()
    {
        int count = JdbcTemplate.update("INSECT INTO users(username1, password) VALUES(?, ?)");
        assertEquals(-1, count);
    }

    @Test
    public void test3()
    {
        int count = JdbcTemplate.update("INSERT INTO users(username1, password) VALUES(?, ?)", "byx", "123456");
        assertEquals(-1, count);
    }
}
