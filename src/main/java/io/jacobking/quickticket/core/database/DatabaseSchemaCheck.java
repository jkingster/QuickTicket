package io.jacobking.quickticket.core.database;

import java.io.File;
import java.sql.*;

public class DatabaseSchemaCheck {

    private static final String[] REQUIRED_TABLE_NAMES = {"COMMENT", "EMAIL", "EMPLOYEE", "JOURNAL", "SCHEMA_VERSION", "TICKET", "ALERT_SETTINGS"};

    private final File       file;
    private       Connection connection;
    private       boolean    isValidDatabase = true;

    public DatabaseSchemaCheck(final File file) {
        this.file = file;
        attemptToConnect();
    }

    public boolean isValidDatabase() {
        return isValidDatabase;
    }

    private void attemptToConnect() {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
            compareSchemas();
        } catch (SQLException e) {
            this.isValidDatabase = false;
        }
    }

    private void compareSchemas() {
        try {
            final DatabaseMetaData meta = connection.getMetaData();
            final ResultSet set = meta.getTables(null, null, null, new String[]{"TABLE"});

            while (set.next()) {
               final String tableName = set.getString("TABLE_NAME");
               if (!containsTable(tableName)) {
                   this.isValidDatabase = false;
                   break;
               }
            }
        } catch (SQLException e) {
            this.isValidDatabase = false;
        }
    }

    private boolean containsTable(final String tableName) {
        for (final String name : REQUIRED_TABLE_NAMES) {
            if (name.equalsIgnoreCase(tableName))
                return true;
        }
        return false;
    }
}
