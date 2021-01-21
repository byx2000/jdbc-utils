package com.byx.jdbc.test;

import com.byx.jdbc.JdbcTemplate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QueryValueTest
{
    @Test
    public void test1()
    {
        Integer count = JdbcTemplate.queryValue("SELECT COUNT(*) FROM users");

        assertNotNull(count);
        assertEquals(5, count);
    }

    @Test
    public void test2()
    {
        Integer count = JdbcTemplate.queryValue("SELECT COUNT(*) FROM users WHERE password = ?", "456");

        assertNotNull(count);
        assertEquals(2, count);
    }

    @Test
    public void test3()
    {
        Integer count = JdbcTemplate.queryValue("SELECT COUNT(*) FROM users WHERE password = ?", "10086");

        assertNotNull(count);
        assertEquals(0, count);
    }

    @Test
    public void test4()
    {
        Integer count = JdbcTemplate.queryValue("SELECT COUNT(*) FRAME users");

        assertNull(count);
    }

    @Test
    public void test5()
    {
        Integer count = JdbcTemplate.queryValue("SELECT COUNT(*) FROM users", 3);

        assertNull(count);
    }
}
