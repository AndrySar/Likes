package services;

/**
 * Created by moiseev on 09.10.17.
 */
public interface LikesService {
    void like(String playerId);
    long getLikes(String playerId);
}
