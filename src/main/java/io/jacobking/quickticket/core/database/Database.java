package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.bridge.BridgeContext;
import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.core.config.impl.FlywayConfig;
import io.jacobking.quickticket.core.config.impl.SystemConfig;
import io.jacobking.quickticket.core.database.repository.RepoCrud;
import io.jacobking.quickticket.gui.alert.Alerts;
import javafx.scene.control.ButtonType;

import java.sql.SQLException;

public class Database {

    private static Database instance = null;

    private final SystemConfig    systemConfig;
    private final FlywayConfig    flywayConfig;
    private final SQLiteConnector sqLiteConnector;
    private final RepoCrud        repoCrud;
    private final BridgeContext   bridgeContext;

    public Database(final SystemConfig systemConfig, final FlywayConfig flywayConfig) {
        this.systemConfig = systemConfig;
        this.flywayConfig = flywayConfig;
        this.sqLiteConnector = new SQLiteConnector(systemConfig);
        runMigrationHandler();

        final JOOQConnector jooqConnector = new JOOQConnector(sqLiteConnector);
        this.repoCrud = new RepoCrud(jooqConnector.getContext());
        this.bridgeContext = new BridgeContext(this);
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database(
                    QuickTicket.getInstance().getSystemConfig(),
                    QuickTicket.getInstance().getFlywayConfig()
            );
        }
        return instance;
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

    private void runMigrationHandler() {
        final boolean hasAutoMigrate = systemConfig.parseBoolean("auto_migrate");
        if (!hasAutoMigrate) {
            return;
        }

        final FlywayMigrator flywayMigrator = FlywayMigrator.init(flywayConfig);
        if (!flywayMigrator.isPendingMigration()) {
            System.out.println("No migrations.");
            return;
        }

        final DatabaseBackup databaseBackup = DatabaseBackup.init(systemConfig.getProperty("db_url"));
        databaseBackup.backup();

        if (databaseBackup.isBackedUp()) {
            flywayMigrator.migrate();
            return;
        }

        Alerts.showWarningConfirmation(
                "Database backup failed. Respond cautiously.",
                "There are pending database migrations. Would you like to proceed? You can manually create a backup as well."
        ).ifPresent(type -> {
            if (type == ButtonType.YES) {
                flywayMigrator.migrate();
            }
        });

    }

    public BridgeContext getBridgeContext() {
        return bridgeContext;
    }
}
