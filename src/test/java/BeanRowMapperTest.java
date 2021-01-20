import com.byx.BeanRowMapper;
import com.byx.JdbcTemplate;
import com.byx.ListResultSetMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class BeanRowMapperTest
{
    @Test
    public void test1()
    {
        List<User> users = JdbcTemplate.query("SELECT * FROM users", new ListResultSetMapper<>(new BeanRowMapper<>(User.class)));

        assertNotNull(users);
        assertEquals(5, users.size());
    }

    @Test
    public void test2()
    {
        String username = "ccc";
        String password = "789";
        List<User> users = JdbcTemplate.query("SELECT * FROM users WHERE username = ? AND password = ?",
                new ListResultSetMapper<>(new BeanRowMapper<>(User.class)),
                username, password);

        assertNotNull(users);
        assertEquals(1, users.size());
    }
}
