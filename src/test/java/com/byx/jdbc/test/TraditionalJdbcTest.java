package com.byx.jdbc.test;

import com.byx.jdbc.JdbcTemplate;
import com.byx.jdbc.test.domain.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TraditionalJdbcTest
{
    @Test
    public void test1()
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try
        {
            conn = JdbcTemplate.getConnection();
            stmt = JdbcTemplate.createPreparedStatement(conn,
                    "SELECT * FROM users WHERE password = ?",
                    "456");
            rs = stmt.executeQuery();
            List<User> users = new ArrayList<>();
            while (rs.next())
            {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }

            assertEquals(2, users.size());
        }
        catch (Exception e)
        {
            fail();
        }
        finally
        {
            JdbcTemplate.close(rs, stmt, conn);
        }
    }

    @Test
    public void test2()
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs;

        // 插入
        try
        {
            conn = JdbcTemplate.getConnection();
            stmt = JdbcTemplate.createPreparedStatement(conn,
                    "INSERT INTO users(username, password) VALUES(?, ?)",
                    "byx", "123456");
            int count = stmt.executeUpdate();

            assertEquals(1, count);
        }
        catch (Exception e)
        {
            fail();
        }
        finally
        {
            JdbcTemplate.close(stmt, conn);
        }

        // 查询
        try
        {
            conn = JdbcTemplate.getConnection();
            stmt = JdbcTemplate.createPreparedStatement(conn, "SELECT * FROM users");
            rs = stmt.executeQuery();
            List<User> users = new ArrayList<>();
            while (rs.next())
            {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }

            assertEquals(6, users.size());
        }
        catch (Exception e)
        {
            fail();
        }
        finally
        {
            JdbcTemplate.close(stmt, conn);
        }

        // 删除
        try
        {
            conn = JdbcTemplate.getConnection();
            stmt = JdbcTemplate.createPreparedStatement(conn,
                    "DELETE FROM users WHERE username = ? AND password = ?",
                    "byx", "123456");
            int count = stmt.executeUpdate();

            assertEquals(1, count);
        }
        catch (Exception e)
        {
            fail();
        }
        finally
        {
            JdbcTemplate.close(stmt, conn);
        }

        // 查询
        try
        {
            conn = JdbcTemplate.getConnection();
            stmt = JdbcTemplate.createPreparedStatement(conn, "SELECT COUNT(*) FROM users");
            rs = stmt.executeQuery();
            int count = rs.getInt(1);

            assertEquals(5, count);
        }
        catch (Exception e)
        {
            fail();
        }
        finally
        {
            JdbcTemplate.close(stmt, conn);
        }
    }
}
