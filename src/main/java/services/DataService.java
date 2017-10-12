package services;

/**
 * Created by moiseev on 12.10.17.
 */
public interface DataService {
    void truncateLikesTable();
    void createPlayer(String playerId);
}
