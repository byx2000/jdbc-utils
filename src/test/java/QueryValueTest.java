import com.byx.JdbcTemplate;
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
        String password = "456";
        Integer count = JdbcTemplate.queryValue("SELECT COUNT(*) FROM users WHERE password = ?", password);

        assertNotNull(count);
        assertEquals(2, count);
    }
}
