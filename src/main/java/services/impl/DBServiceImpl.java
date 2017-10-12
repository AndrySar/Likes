package services.impl;

import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import services.DBService;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by moiseev on 09.10.17.
 */
public class DBServiceImpl implements DBService {

    private final static BasicDataSource dataSource;

    static {
        Properties properties = new Properties();
        try (InputStream input = getConfig()) {
            properties.load(input);
        } catch (IOException e) {
            System.out.println("Config file not found");
        }

        dataSource = new BasicDataSource();
        dataSource.setDriverClassName(properties.getProperty("db.driver"));
        dataSource.setUrl(properties.getProperty("db.url"));
        dataSource.setUsername(properties.getProperty("db.user"));
        dataSource.setPassword(properties.getProperty("db.password"));
        dataSource.setMaxIdle(10);

        Flyway flyway = new Flyway();
        flyway.setBaselineOnMigrate(true);
        flyway.setLocations("db/migration");
        flyway.setDataSource(dataSource);
        flyway.migrate();
    }

    private static InputStream getConfig() throws FileNotFoundException {
        InputStream input;
        String configFileName = System.getProperty("config");
        if (configFileName != null) {
            input = new FileInputStream(configFileName);
        } else {
            input = DBServiceImpl.class.getClassLoader().getResourceAsStream("config.properties");
        }
        return input;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
