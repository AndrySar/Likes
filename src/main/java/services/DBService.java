package services;

import javax.sql.DataSource;

/**
 * Created by moiseev on 09.10.17.
 */
public interface DBService {
    DataSource getDataSource();
}
