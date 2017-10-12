import org.junit.*;
import org.junit.runner.RunWith;
import services.DBService;
import services.DataService;
import services.LikesService;
import services.impl.DBServiceImpl;
import services.impl.DataServiceImpl;
import services.impl.LikesServiceImpl;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.annotation.Repeatable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

/**
 * Created by moiseev on 09.10.17.
 */

@RunWith(RepeatingTestRunner.class)
public class LikesTest {

    private static LikesService likesService;
    private static DataService dataService;
    private final static List<String> players = Arrays.asList("1", "2", "3", "4", "5", "6");

    @BeforeClass
    public static void setup() throws SQLException {
        DBService dbService = new DBServiceImpl();
        likesService = new LikesServiceImpl(dbService.getDataSource());
        dataService = new DataServiceImpl(dbService.getDataSource());
        dataService.truncateLikesTable();
    }

    @Before
    public void clearLikesTable() {
        dataService.truncateLikesTable();

        for(String player : players) {
            dataService.createPlayer(player);
        }
    }

    @Test
    public void simpleTest() throws SQLException {
        likesService.like("1");
        assertEquals(likesService.getLikes("1"), 1);
    }

    @Test
    public void doubleTest() throws SQLException {
        likesService.like("2");
        likesService.like("2");
        assertEquals(likesService.getLikes("2"), 2);
    }

    @Test
    public void zeroTest() throws SQLException {
        assertEquals(likesService.getLikes("1"), 0);
    }

    @Test
    @Repeating()
    public void concurrentTest() {
        final List<Runnable> tasks = new ArrayList<>();

        Runnable task = () -> { for(String player : players) {
            likesService.like(player);
        }};

        ExecutorService executorService = Executors.newFixedThreadPool(players.size());

        final ArrayList<Future<?>> likeFutures = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            final Future<?> likeFuture = executorService.submit(task);
            likeFutures.add(likeFuture);
        }

        for (Future<?> future : likeFutures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new IllegalStateException("Threads failed", e);
            }
        }

        int follow = players.size();
        for(String player : players) {
            assertEquals(likesService.getLikes(player), follow);
        }
    }



}
