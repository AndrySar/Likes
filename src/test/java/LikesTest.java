import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import services.LikesService;
import services.impl.DBServiceImpl;
import services.impl.LikesServiceImpl;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.annotation.Repeatable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        assertEquals(likesService.getLikes("3"), 0);
    }

    @Test
    @Repeat(10)
    public void concurrentTest() {
        final List<Runnable> tasks = new ArrayList<>();
        final List<String> playerId = new ArrayList<>();
        playerId.add("3");
        playerId.add("4");
        playerId.add("5");
        playerId.add("6");
        playerId.add("7");
        playerId.add("8");
        playerId.add("9");
        playerId.add("10");
        playerId.add("11");
        playerId.add("12");
        playerId.add("13");
        playerId.add("14");
        playerId.add("15");
        playerId.add("16");


        Runnable task = () -> { for(String player : playerId) {
            likesService.like(player);
        }};

        ExecutorService executorService = Executors.newFixedThreadPool(playerId.size());

        final ArrayList<Future<?>> likeFutures = new ArrayList<>();
        for (int i = 0; i < playerId.size(); i++) {
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

        assertEquals(likesService.getLikes("16"), 14);
        assertEquals(likesService.getLikes("11"), 14);
        assertEquals(likesService.getLikes("3"), 14);
        assertEquals(likesService.getLikes("5"), 14);
    }



}
