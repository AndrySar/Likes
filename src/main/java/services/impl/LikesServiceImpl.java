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
//            String query = new StringBuilder("INSERT INTO ")
//                    .append("like ")
//                    .append("(playerId, likes) VALUES(?, ?) ")
//                    .append("ON DUPLICATE KEY UPDATE likes = likes + 1 ")
//                    .append("WHERE playerId = ?;").toString();

            String query = new StringBuilder()
                    .append("UPDATE like_table SET likes = likes + 1 WHERE playerid=?;")
                    .append("INSERT INTO like_table (playerid, likes) SELECT ?, ? WHERE NOT EXISTS (SELECT * FROM like_table WHERE playerid = ? );")
                    .toString();

            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, playerId);
                ps.setString(2, playerId);
                ps.setInt(3, 1);
                ps.setString(4, playerId);

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
