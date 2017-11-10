package com.ef.db;

import com.ef.AppConfig;
import com.ef.exceptions.ConfigFileException;
import com.ef.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Factory to provide db connection.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public class ConnectionFactory {


    private ConnectionFactory() {
    }

    public static Connection newConnection() throws DatabaseException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = null;
            connection = DriverManager.getConnection(AppConfig.getConfig().getDbURL(),
                    AppConfig.getConfig().getDbUser(),
                    AppConfig.getConfig().getDbPassword());

            return connection;
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Driver class is not found", e);
        } catch (SQLException e) {
            throw new DatabaseException("Sql exception", e);
        } catch (ConfigFileException e) {
            throw new DatabaseException("Missing configuration file", e);
        }
    }

}
