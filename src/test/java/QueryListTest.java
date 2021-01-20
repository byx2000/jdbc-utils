import com.byx.BeanRowMapper;
import com.byx.JdbcTemplate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QueryListTest
{
    @Test
    public void test1()
    {
        List<User> users = JdbcTemplate.queryList("SELECT * FROM users", new BeanRowMapper<>(User.class));

        assertNotNull(users);
        assertEquals(5, users.size());
    }

    @Test
    public void test2()
    {
        String password = "456";
        List<User> users = JdbcTemplate.queryList("SELECT * FROM users WHERE password = ?",
                new BeanRowMapper<>(User.class),
                password);

        assertNotNull(users);
        assertEquals(2, users.size());
    }
}
