import com.byx.JdbcTemplate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class CustomResultSetMapperTest
{
    @Test
    public void test1()
    {
        List<User> users = JdbcTemplate.query("SELECT * FROM users", rs ->
        {
            List<User> us = new ArrayList<>();
            while (rs.next())
            {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                us.add(u);
            }
            return us;
        });

        assertNotNull(users);
        assertEquals(5, users.size());
    }

    @Test
    public void test2()
    {
        String username = "ccc";
        String password = "789";

        List<User> users = JdbcTemplate.query("SELECT * FROM users WHERE username = ? AND password = ?", rs ->
        {
            List<User> us = new ArrayList<>();
            while (rs.next())
            {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                us.add(u);
            }
            return us;
        }, username, password);

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("ccc", users.get(0).getUsername());
        assertEquals("789", users.get(0).getPassword());
    }

    @Test
    public void test3()
    {
        Integer count = JdbcTemplate.query("SELECT COUNT(*) FROM users", rs -> rs.getInt(1));
        assertEquals(5, count);
    }

    @Test
    public void test4()
    {
        Integer count = JdbcTemplate.query("SELECT COUNT(*) FROM users WHERE password = '456'", rs -> rs.getInt(1));
        assertEquals(2, count);
    }

    @Test
    public void test5()
    {
        List<User> users = JdbcTemplate.query("SELECT * FROM users WHERE id = 1001", rs ->
        {
            List<User> us = new ArrayList<>();
            while (rs.next())
            {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                us.add(u);
            }
            return us;
        });

        assertNotNull(users);
        assertEquals(0, users.size());
    }

    @Test
    public void test6()
    {
        List<User> users = JdbcTemplate.query("SELET * FROM users", rs ->
        {
            List<User> us = new ArrayList<>();
            while (rs.next())
            {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                us.add(u);
            }
            return us;
        });

        assertNull(users);
    }

    @Test
    public void test7()
    {
        List<User> users = JdbcTemplate.query("SELECT * FROM users", rs ->
        {
            List<User> us = new ArrayList<>();
            while (rs.next())
            {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username1"));
                u.setPassword(rs.getString("password"));
                us.add(u);
            }
            return us;
        });

        assertNull(users);
    }
}
