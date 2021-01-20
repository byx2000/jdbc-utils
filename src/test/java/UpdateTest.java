import com.byx.BeanRowMapper;
import com.byx.JdbcTemplate;
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
    }
}
