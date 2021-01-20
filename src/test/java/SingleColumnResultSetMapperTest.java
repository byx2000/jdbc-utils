import com.byx.JdbcTemplate;
import com.byx.SingleColumnResultSetMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SingleColumnResultSetMapperTest
{
    @Test
    public void test1()
    {
        Integer count = JdbcTemplate.query("SELECT COUNT(*) FROM users", new SingleColumnResultSetMapper<>());

        assertNotNull(count);
        assertEquals(5, count);
    }

    @Test
    public void test2()
    {
        String password = "456";
        Integer count = JdbcTemplate.query("SELECT COUNT(*) FROM users WHERE password = ?",
                new SingleColumnResultSetMapper<>(),
                password);

        assertNotNull(count);
        assertEquals(2, count);
    }
}
