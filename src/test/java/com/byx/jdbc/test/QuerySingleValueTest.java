package com.byx.jdbc.test;

import com.byx.jdbc.JdbcTemplate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuerySingleValueTest
{
    @Test
    public void test1()
    {
        Integer count = JdbcTemplate.querySingleValue("SELECT COUNT(*) FROM users", Integer.class);

        assertNotNull(count);
        assertEquals(5, count);
    }

    @Test
    public void test2()
    {
        Integer count = JdbcTemplate.querySingleValue("SELECT COUNT(*) FROM users WHERE password = ?",
                Integer.class,
                "456");

        assertNotNull(count);
        assertEquals(2, count);
    }

    @Test
    public void test3()
    {
        Integer count = JdbcTemplate.querySingleValue("SELECT COUNT(*) FROM users WHERE password = ?",
                Integer.class,
                "10086");

        assertNotNull(count);
        assertEquals(0, count);
    }

    @Test
    public void test4()
    {
        Integer count = JdbcTemplate.querySingleValue("SELECT COUNT(*) FRAME users", Integer.class);

        assertNull(count);
    }

    @Test
    public void test5()
    {
        Integer count = JdbcTemplate.querySingleValue("SELECT COUNT(*) FROM users",
                Integer.class,
                3);

        assertNull(count);
    }
}
