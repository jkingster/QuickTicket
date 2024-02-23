package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.App;
import io.jacobking.quickticket.gui.alert.Notify;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseMigrator {

    private static final String MIGRATION_PATH = "sql/migration/v%d.%d.SQL";
    private static final String SCHEMA_FETCH   = "SELECT MAJOR, MINOR FROM SCHEMA_VERSION WHERE ID = 0;";

    private static final DatabaseVersion targetDatabaseVersion = new DatabaseVersion(1, 0);

    private final DatabaseVersion currentDatabaseVersion = new DatabaseVersion();
    private final Connection      connection;

    public DatabaseMigrator(final Connection connection) {
        this.connection = connection;
    }

    public void migrate() {
        loadSchemaVersion();
        if (currentDatabaseVersion.isNegative()) {
            Notify.showError(
                    "Database Fetch Failed",
                    "Failed to parse current schema version.",
                    "Please reload QuickTicket, or submit a bug report."
            );
            return;
        }

        System.out.println(currentDatabaseVersion);

        final int migration = checkMigration();
        if (migration < 0) {
            startMigration();
        }
    }

    private int checkMigration() {
        final int currentMajor = currentDatabaseVersion.getMajor();
        if (currentMajor == -1)
            return -1;

        final int expectedMajor = targetDatabaseVersion.getMajor();
        if (currentMajor != expectedMajor) {
            return Integer.compare(currentMajor, expectedMajor);
        }

        final int currentMinor = currentDatabaseVersion.getMinor();
        final int expectedMinor = targetDatabaseVersion.getMinor();
        return Integer.compare(currentMinor, expectedMinor);
    }

    private void startMigration() {
        final int currentMajor = currentDatabaseVersion.getMajor();
        final int currentMinor = currentDatabaseVersion.getMinor();
        final int expectedMajor = targetDatabaseVersion.getMajor();
        final int expectedMinor = targetDatabaseVersion.getMinor();

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
        final String convertedMigrationPath = MIGRATION_PATH.formatted(majorVersion, minorVersion);
        final InputStream sqlStream = getSQLStream(convertedMigrationPath);
        if (sqlStream == null) {
            Notify.showError("Failed to migrate database.", "Failed to retrieve InputStream of the migration path.", "Please submit bug report.");
            return;
        }

        final String query = getConvertedSQLStream(sqlStream);
        if (query.isEmpty()) {
            Notify.showError("Failed to migrate database.", "Failed to retrieve InputStream as query string.", "Please submit bug report.");
            return;
        }

        final String[] tokenizeQueries = tokenizeQuery(query);
        processTokenizedQueries(tokenizeQueries);
    }

    private InputStream getSQLStream(final String convertedMigrationPath) {
        return App.class.getResourceAsStream(convertedMigrationPath);
    }

    private String getConvertedSQLStream(final InputStream stream) {
        try {
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Notify.showException("Failed to convert SQL stream.", e.fillInStackTrace());
        }
        return "";
    }

    private String[] tokenizeQuery(final String baseQuery) {
        return baseQuery.split(";");
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
            Notify.showException("Failed to execute query for migration.", e.fillInStackTrace());
        }
    }

    private void loadSchemaVersion() {
        try (final Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(SCHEMA_FETCH);
            while (resultSet.next()) {
                this.currentDatabaseVersion.setMajor(resultSet.getInt(1));
                this.currentDatabaseVersion.setMinor(resultSet.getInt(2));
            }
        } catch (SQLException e) {
            Notify.showException("Failed to load targeted schema version.", e.fillInStackTrace());
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
            return "DatabaseVersion{" +
                    "major=" + major +
                    ", minor=" + minor +
                    '}';
        }
    }
}
