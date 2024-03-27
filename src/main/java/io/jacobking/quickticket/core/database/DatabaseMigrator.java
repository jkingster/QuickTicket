package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.App;
import io.jacobking.quickticket.core.Config;
import io.jacobking.quickticket.core.utility.FileIO;
import io.jacobking.quickticket.gui.alert.Alerts;
import javafx.scene.control.ButtonType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class DatabaseMigrator {

    private static final String MIGRATION_PATH = "sql/migration/v%d.%d.SQL";
    private static final String SCHEMA_FETCH   = "SELECT MAJOR, MINOR FROM SCHEMA_VERSION WHERE ID = 0;";

    private static final DatabaseVersion TARGET_DATABASE_VERSION  = new DatabaseVersion(1, 4);
    private static       DatabaseVersion CURRENT_DATABASE_VERSION = null;

    private final DatabaseVersion currentDatabaseVersion = new DatabaseVersion();
    private final Connection      connection;

    public DatabaseMigrator(final Connection connection) {
        this.connection = connection;
    }

    public static String getCurrentDatabaseVersion() {
        if (CURRENT_DATABASE_VERSION == null)
            return "undefined";
        return CURRENT_DATABASE_VERSION.toString();
    }

    public void migrate() {
        loadSchemaVersion();
        if (currentDatabaseVersion.isNegative()) {
            Alerts.showError(
                    "Database Fetch Failed",
                    "Failed to parse current schema version.",
                    "Please reload QuickTicket, or submit a bug report."
            );
            return;
        }

        final int migration = checkMigration();
        if (migration >= 0)
            return;

        if (!backupCurrent()) {
            final Optional<ButtonType> warning = Alerts.showWarningConfirmation(
                    "Backing up your current database failed.",
                    "We recommend making a copy manually before proceeding. Do you still want to run the migrate process?"
            );

            warning.ifPresent(type -> {
                if (type == ButtonType.YES) {
                    startMigration();
                }
            });
            return;
        }

        startMigration();
    }

    private boolean backupCurrent() {
        final File current = new File(Config.getInstance().readProperty("db_url"));
        if (!current.exists() || !current.isFile())
            return false;

        final String dbName = current.getName();
        final String timestamp = String.valueOf(System.currentTimeMillis());
        final String newName = String.format("backup-%s-%s.db", dbName.replace(".db", ""), timestamp);
        final String targetPath = String.format("%s%s", FileIO.TARGET_COPY_PATH, newName);

        try {
            FileUtils.copyFile(current, new File(targetPath));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private int checkMigration() {
        final int currentMajor = currentDatabaseVersion.getMajor();
        if (currentMajor == -1)
            return -1;

        final int expectedMajor = TARGET_DATABASE_VERSION.getMajor();
        if (currentMajor != expectedMajor) {
            return Integer.compare(currentMajor, expectedMajor);
        }

        final int currentMinor = currentDatabaseVersion.getMinor();
        final int expectedMinor = TARGET_DATABASE_VERSION.getMinor();
        return Integer.compare(currentMinor, expectedMinor);
    }

    private void startMigration() {
        final int currentMajor = currentDatabaseVersion.getMajor();
        final int currentMinor = currentDatabaseVersion.getMinor();
        final int expectedMajor = TARGET_DATABASE_VERSION.getMajor();
        final int expectedMinor = TARGET_DATABASE_VERSION.getMinor();


        for (int majorVersion = currentMajor; majorVersion <= expectedMajor; majorVersion++) {
            int startMinor = currentMinor;
            if (majorVersion == currentMajor) {
                startMinor = currentMinor + 1;
            }

            for (int minorVersion = startMinor; minorVersion <= expectedMinor; minorVersion++) {
                processVersion(majorVersion, minorVersion);
            }
        }
    }

    private void processVersion(final int majorVersion, final int minorVersion) {
        System.out.println("PROCESSING " + majorVersion + " " + " " + minorVersion);
        final String convertedMigrationPath = MIGRATION_PATH.formatted(majorVersion, minorVersion);
        final InputStream sqlStream = getSQLStream(convertedMigrationPath);
        if (sqlStream == null) {
            Alerts.showError("Failed to migrate database.", "Failed to retrieve InputStream of the migration path.", "Please submit bug report.");
            return;
        }

        final String queryString = getConvertedSQLStream(sqlStream);
        if (queryString.isEmpty()) {
            Alerts.showError("Failed to migrate database.", "Failed to retrieve InputStream as query string.", "Please submit bug report.");
            return;
        }

        final String[] tokenizeQueries = tokenizeQuery(queryString);
        processTokenizedQueries(tokenizeQueries);
    }

    private InputStream getSQLStream(final String convertedMigrationPath) {
        return App.class.getResourceAsStream(convertedMigrationPath);
    }

    private String getConvertedSQLStream(final InputStream stream) {
        try {
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Alerts.showException("Failed to convert SQL stream.", e.fillInStackTrace());
        }
        return "";
    }

    private String[] tokenizeQuery(final String baseQuery) {
        return baseQuery.split(SQLLoader.DELIMITER);
    }

    private void processTokenizedQueries(final String[] queries) {
        for (final String query : queries) {
            executeQuery(query);
        }
    }

    private void executeQuery(final String query) {
        try (final Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            Alerts.showException(
                    String.format("Failed to execute query for target migration: %s", TARGET_DATABASE_VERSION),
                    e.fillInStackTrace()
            );
        }
    }

    private void loadSchemaVersion() {
        try (final Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(SCHEMA_FETCH);
            while (resultSet.next()) {
                this.currentDatabaseVersion.setMajor(resultSet.getInt(1));
                this.currentDatabaseVersion.setMinor(resultSet.getInt(2));
                DatabaseMigrator.CURRENT_DATABASE_VERSION = currentDatabaseVersion;
            }
        } catch (SQLException e) {
            Alerts.showException(
                    String.format("Failed to load schema version: Target migration: %s", TARGET_DATABASE_VERSION),
                    e.fillInStackTrace()
            );
        }
    }

    static class DatabaseVersion {
        private int major;
        private int minor;

        public DatabaseVersion(final int major, final int minor) {
            this.major = major;
            this.minor = minor;
        }

        public DatabaseVersion() {
            this.major = -1;
            this.minor = -1;
        }

        public int getMajor() {
            return major;
        }

        public void setMajor(int major) {
            this.major = major;
        }

        public int getMinor() {
            return minor;
        }

        public void setMinor(int minor) {
            this.minor = minor;
        }

        public boolean isNegative() {
            return major == -1 && minor == -1;
        }

        @Override public String toString() {
            return String.format("v%d.%d", major, minor);
        }
    }
}
