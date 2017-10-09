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

    DataSource dataSource = DBServiceImpl.getDataSource();
    @Override
    public void like(String playerId) {
        try (Connection connection = dataSource.getConnection()) {
            String query = new StringBuilder("INSERT INTO ")
                    .append("like ")
                    .append("(playerId, likes) VALUES(?, ?) ")
                    .append("ON DUPLICATE KEY UPDATE likes = likes + 1 ")
                    .append("WHERE playerId = ?;").toString();

            String query = new StringBuilder()
            .append("UPDATE table SET playerId=, field2='Z' WHERE id=3;")
                    .append("INSERT INTO table (id, field, field2) ")
            INSERT INTO table (id, field, field2)
            SELECT 3, 'C', 'Z'
            WHERE NOT EXISTS (SELECT 1 FROM table WHERE id=3);
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, playerId);
                ps.setInt(2, 0);
                ps.setString(3, playerId);

                try (ResultSet resultSet = ps.executeQuery()) {
                    resultSet.next();
                }
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
            String query = new StringBuilder("SELECT * FROM like WHERE like.playerId = ?;").toString();
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, playerId);

                try (ResultSet resultSet = ps.executeQuery()) {
                    if(resultSet.next()) {
                        return resultSet.getLong("playerId");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Long.valueOf(-1);
    }
}
