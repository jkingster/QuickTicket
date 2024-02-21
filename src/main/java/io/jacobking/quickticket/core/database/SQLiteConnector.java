package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.core.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnector {
    private static final String DRIVER_NAME = "org.sqlite.JDBC";

    private static final String     FORMATTED_DB_URL = "jdbc:sqlite:%s";
    private static final String     DB_URL           = "db_url";
    private final        Config     config;
    private              Connection connection;

    public SQLiteConnector(final Config config) {
        this.config = config;
        establishConnection();
    }

    private void establishConnection() {
        try {
            Class.forName(DRIVER_NAME);
            final String databaseUrl = FORMATTED_DB_URL.formatted(config.readProperty(DB_URL));
            this.connection = DriverManager.getConnection(databaseUrl);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
