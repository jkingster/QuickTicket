package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.bridge.BridgeContext;
import io.jacobking.quickticket.core.config.FlywayConfig;
import io.jacobking.quickticket.core.config.SystemConfig;
import io.jacobking.quickticket.core.database.repository.RepoCrud;
import io.jacobking.quickticket.core.utility.Logs;
import io.jacobking.quickticket.gui.alert.Alerts;
import javafx.scene.control.ButtonType;

import java.sql.SQLException;

public class Database {

    private final SystemConfig    systemConfig;
    private final FlywayConfig    flywayConfig;
    private final SQLiteConnector sqLiteConnector;
    private final RepoCrud        repoCrud;
    private final BridgeContext   bridgeContext;

    private final boolean isConfigured;

    public Database(final SystemConfig systemConfig, final FlywayConfig flywayConfig) {
        Logs.info("Database Initialized.");
        this.systemConfig = systemConfig;
        this.flywayConfig = flywayConfig;
        this.sqLiteConnector = new SQLiteConnector(systemConfig);
        this.isConfigured = sqLiteConnector.hasConnection();
        checkForMigration();

        final JOOQConnector jooqConnector = new JOOQConnector(sqLiteConnector);
        this.repoCrud = new RepoCrud(jooqConnector.getContext());
        this.bridgeContext = new BridgeContext(this);
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

    private void checkForMigration() {
        if (sqLiteConnector.getConnection() == null) {
            Alerts.showErrorOverride("Failed to establish sqlite connector.", "Please report this.");
            return;
        }

        final boolean hasAutoMigrate = systemConfig.parseBoolean("auto_migration");
        if (!hasAutoMigrate) {
            Logs.info("Auto-migration is disabled in system.properties.");
            return;
        }

        final FlywayMigrator flywayMigrator = FlywayMigrator.init(flywayConfig);
        if (!flywayMigrator.isPendingMigration()) {
            Logs.info("There are no pending migrations!");
            return;
        }

        final String url = systemConfig.getProperty("database_url");
        final boolean success = new DatabaseBackup(url)
                .setDestination()
                .buildBackup()
                .isSuccessful();

        if (success) {
            flywayMigrator.migrate();
            return;
        }

        Logs.warn("Failed to create backup pre-migration!");
        Alerts.showWarningConfirmation(
                "Database backup failed. Respond cautiously.",
                "There are pending database migrations. Would you like to proceed? You can manually create a backup before proceeding."
        ).ifPresent(type -> {
            if (type == ButtonType.YES) {
                flywayMigrator.migrate();
            }
        });

    }

    public BridgeContext getBridgeContext() {
        return bridgeContext;
    }

    public boolean isConfigured() {
        return isConfigured;
    }
}
