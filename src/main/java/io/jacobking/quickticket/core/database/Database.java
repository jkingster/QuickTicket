package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.core.config.SystemConfig;
import io.jacobking.quickticket.core.database.repository.RepoCrud;
import io.jacobking.quickticket.core.utility.Logs;
import io.jacobking.quickticket.gui.alert.Announcements;

import java.io.File;
import java.sql.SQLException;

public class Database {

    private static final String WARNING_MESSAGE = "There are pending database migrations, and a routine backup measure failed. " +
            "It is recommended you manually create a backup internally before proceeding.";

    private final SystemConfig    systemConfig;
    private final SQLiteConnector sqLiteConnector;
    private final RepoCrud        repoCrud;


    public Database(final SystemConfig systemConfig) {
        Logs.info("Database Initialized.");
        this.systemConfig = systemConfig;
        this.sqLiteConnector = new SQLiteConnector(systemConfig);
        final JOOQConnector jooqConnector = new JOOQConnector(sqLiteConnector);
        this.repoCrud = new RepoCrud(jooqConnector.getContext());
    }

    public RepoCrud call() {
        return repoCrud;
    }

    public void close() {
        try {
            sqLiteConnector.getConnection().close();
        } catch (SQLException e) {
            // TODO: alert
        }
    }

    public void checkForMigration() {
        if (sqLiteConnector.getConnection() == null) {
            Announcements.get().showErrorOverride("Failed to establish sqlite connector.", "Please report this.");
            return;
        }

        final boolean hasAutoMigrate = systemConfig.parseBoolean("auto_migration");
        if (!hasAutoMigrate) {
            Logs.info("Auto-migration is disabled in system.properties.");
            return;
        }

        final FlywayMigrator flywayMigrator = new FlywayMigrator(systemConfig);
        if (!flywayMigrator.isPendingMigration()) {
            Logs.info("There are no pending migrations!");
            return;
        }

        if (systemConfig.parseBoolean("first_launch")) {
            flywayMigrator.migrate();
            return;
        }


        final String url = systemConfig.getProperty("database_url");
        final File databaseFile = new File(url);
        if (!databaseFile.exists() || !databaseFile.isFile()) {
            Logs.warn("Target path is not of database file type or does not exist.");
            return;
        }

        flywayMigrator.migrate();

    }


    public boolean hasConnection() {
        return sqLiteConnector.hasConnection();
    }

}
