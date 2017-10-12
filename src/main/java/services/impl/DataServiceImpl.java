package services.impl;

import services.DataService;

import javax.sql.DataSource;
import javax.xml.ws.ServiceMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by moiseev on 12.10.17.
 */

public class DataServiceImpl implements DataService {
    private DataSource dataSource;

    public DataServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void truncateLikesTable() {
        try (Connection connection = dataSource.getConnection()) {
            String query = new StringBuilder("TRUNCATE TABLE like_table;").toString();
            try (Statement ps = connection.createStatement()) {
                ps.execute(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createPlayer(String playerId) {
        try (Connection connection = dataSource.getConnection()) {
            String query = new StringBuilder("INSERT INTO like_table (playerid, likes) VALUES (?, ?);").toString();

            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, playerId);
                ps.setInt(2, 0);

                ps.execute();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
