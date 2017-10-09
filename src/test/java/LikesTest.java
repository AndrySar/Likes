import org.junit.Before;
import org.junit.Test;
import services.LikesService;
import services.impl.DBServiceImpl;
import services.impl.LikesServiceImpl;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.*;

/**
 * Created by moiseev on 09.10.17.
 */
public class LikesTest {

    private LikesService likesService;
    @Before
    public void setup() throws SQLException {
        likesService = new LikesServiceImpl();
    }

    @Test
    public void test() throws SQLException {
        likesService.like("1");
        assertEquals(likesService.getLikes("1"), 1);
    }
}
