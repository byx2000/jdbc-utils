import com.byx.JdbcTemplate;
import com.byx.ListResultSetMapper;
import com.byx.MapRowMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

public class MapRowMapperTest
{
    @Test
    public void test1()
    {
        List<Map<String, Object>> users = JdbcTemplate.query("SELECT * FROM users", new ListResultSetMapper<>(new MapRowMapper()));

        assertNotNull(users);
        assertEquals(5, users.size());
        assertTrue(users.get(0).containsKey("id"));
        assertTrue(users.get(0).containsKey("username"));
        assertTrue(users.get(0).containsKey("password"));
    }

    @Test
    public void test2()
    {
        String username = "ccc";
        String password = "789";
        List<Map<String, Object>> users = JdbcTemplate.query("SELECT * FROM users WHERE username = ? AND password = ?",
                new ListResultSetMapper<>(new MapRowMapper()),
                username, password);

        assertNotNull(users);
        assertEquals(1, users.size());
        assertTrue(users.get(0).containsKey("id"));
        assertTrue(users.get(0).containsKey("username"));
        assertTrue(users.get(0).containsKey("password"));
    }
}
