import com.byx.JdbcTemplate;
import com.byx.ListResultSetMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomRowMapperTest
{
    @Test
    public void test1()
    {
        List<User> users = JdbcTemplate.query("SELECT * FROM users", new ListResultSetMapper<>(rs ->
        {
            User u = new User();
            u.setId(rs.getInt("id"));
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("password"));
            return u;
        }));

        assertNotNull(users);
        assertEquals(5, users.size());
    }

    @Test
    public void test2()
    {
        String username = "ccc";
        String password = "789";
        List<User> users = JdbcTemplate.query("SELECT * FROM users WHERE username = ? AND password = ?", new ListResultSetMapper<>(rs ->
        {
            User u = new User();
            u.setId(rs.getInt("id"));
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("password"));
            return u;
        }), username, password);

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("ccc", users.get(0).getUsername());
        assertEquals("789", users.get(0).getPassword());
    }

    @Test
    public void test3()
    {
        List<String> usernames = JdbcTemplate.query("SELECT * FROM users", new ListResultSetMapper<>(rs -> rs.getString("username")));

        assertNotNull(usernames);
        assertEquals(5, usernames.size());
    }

    @Test
    public void test4()
    {
        List<User> users = JdbcTemplate.query("SELECT * FROM users WHERE id = 1001", new ListResultSetMapper<>(rs ->
        {
            User u = new User();
            u.setId(rs.getInt("id"));
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("password"));
            return u;
        }));

        assertNotNull(users);
        assertEquals(0, users.size());
    }
}
