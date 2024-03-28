package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.core.config.impl.SystemConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnector {
    private static final String DRIVER_NAME = "org.sqlite.JDBC";

    private static final String     FORMATTED_DB_URL = "jdbc:sqlite:%s";
    private static final String       DB_URL           = "db_url";
    private final        SystemConfig config;
    private              Connection   connection;

    public SQLiteConnector(final SystemConfig config) {
        this.config = config;
        establishConnection();
    }

    private void establishConnection() {
        try {
            Class.forName(DRIVER_NAME);
            System.out.println(config.getProperty(DB_URL));
            final String databaseUrl = FORMATTED_DB_URL.formatted(config.getProperty(DB_URL));
            this.connection = DriverManager.getConnection(databaseUrl);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
