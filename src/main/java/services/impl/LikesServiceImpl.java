package services.impl;

import services.LikesService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by moiseev on 09.10.17.
 */
public class LikesServiceImpl implements LikesService {

    private DataSource dataSource;

    public LikesServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void like(String playerId) {
        try (Connection connection = dataSource.getConnection()) {
            // выполняем инкремент лайков игрока. Существует игрок или нет мы проверить не можем, нет сервисов работы с аккаунтами.
            String query = new StringBuilder()
                    .append("UPDATE like_table SET likes = likes + 1 WHERE playerid=?;")
                    .toString();

            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, playerId);

                ps.execute();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getLikes(String playerId) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            String query = new StringBuilder("SELECT * FROM like_table WHERE like_table.playerid = ?;").toString();
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, playerId);

                try (ResultSet resultSet = ps.executeQuery()) {
                    if(resultSet.next()) {
                        return resultSet.getLong("likes");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Long.valueOf(0);
    }
}
