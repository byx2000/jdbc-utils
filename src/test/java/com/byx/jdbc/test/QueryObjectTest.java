package com.byx.jdbc.test;

import com.byx.jdbc.JdbcTemplate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QueryObjectTest
{
    @Test
    public void test1()
    {
        User user = JdbcTemplate.queryObject("SELECT * FROM users WHERE id = 2", User.class);

        assertNotNull(user);
        assertEquals(2, user.getId());
        assertEquals("bbb", user.getUsername());
        assertEquals("456", user.getPassword());
    }

    @Test
    public void test2()
    {
        User user = JdbcTemplate.queryObject("SELECT * FROM users WHERE id = ?", User.class, "2");

        assertNotNull(user);
        assertEquals(2, user.getId());
        assertEquals("bbb", user.getUsername());
        assertEquals("456", user.getPassword());
    }

    @Test
    public void test3()
    {
        User user = JdbcTemplate.queryObject("SELECT * FROM users WHERE id = 1001", User.class);

        assertNull(user);
    }

    @Test
    public void test4()
    {
        User user = JdbcTemplate.queryObject("SELECT * FROM users WEAR id = 2", User.class);

        assertNull(user);
    }

    @Test
    public void test5()
    {
        User user = JdbcTemplate.queryObject("SELECT * FROM users WEAR id = 2", User.class, 2);

        assertNull(user);
    }
}
