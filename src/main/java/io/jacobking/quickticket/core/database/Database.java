package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.core.config.impl.SystemConfig;
import io.jacobking.quickticket.core.database.repository.RepoCrud;
import io.jacobking.quickticket.gui.alert.Alerts;
import javafx.scene.control.ButtonType;

import java.sql.SQLException;

public class Database {

    private static Database instance = null;

    private final SQLiteConnector sqLiteConnector;
    private final RepoCrud        repoCrud;

    private Database() {
        this.sqLiteConnector = new SQLiteConnector(SystemConfig.getInstance());
        SQLLoader.process(sqLiteConnector.getConnection());

        runMigrationHandler();

        final JOOQConnector jooqConnector = new JOOQConnector(sqLiteConnector);
        this.repoCrud = new RepoCrud(jooqConnector.getContext());
    }

    public static Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    public static void rebuildInstance() {
        instance = null;
        getInstance();
    }

    public static RepoCrud call() {
        return getInstance().repoCrud;
    }

    public void close() {
        try {
            sqLiteConnector.getConnection().close();
        } catch (SQLException e) {
            // TODO: alert
        }
    }

    private void runMigrationHandler() {
        final boolean hasAutoMigrate = SystemConfig.getInstance()
                .getProperty("auto_migrate", boolean.class, false);

        if (!hasAutoMigrate)
            return;

        final FlywayMigrator flywayMigrator = FlywayMigrator.init();
        if (!flywayMigrator.isPendingMigration())
            return;

        final DatabaseBackup databaseBackup = DatabaseBackup.init();
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
}
