package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.core.config.SystemConfig;
import io.jacobking.quickticket.core.utility.Logs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnector {
    private static final String DRIVER_NAME = "org.sqlite.JDBC";

    private static final String       FORMATTED_DB_URL = "jdbc:sqlite:%s";
    private static final String       DB_URL           = "database_url";
    private final        SystemConfig config;
    private              Connection   connection;

    public SQLiteConnector(final SystemConfig config) {
        Logs.info("SQLite Connector initialized.");
        this.config = config;
        establishConnection();
    }

    private void establishConnection() {
        try {
            Class.forName(DRIVER_NAME);
            final String databaseUrl = FORMATTED_DB_URL.formatted(config.getProperty(DB_URL));
            this.connection = DriverManager.getConnection(databaseUrl);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean hasConnection() {
        return this.connection != null;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            Logs.warn(e.getMessage());
        }
    }

}
