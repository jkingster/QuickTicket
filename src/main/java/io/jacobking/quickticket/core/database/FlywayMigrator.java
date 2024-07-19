package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.core.config.FlywayConfig;
import io.jacobking.quickticket.gui.alert.AlertPopup;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.callback.Context;
import org.flywaydb.core.api.callback.Event;

import java.io.File;
import java.sql.SQLException;

public class FlywayMigrator {

    private static final String WARNING_MESSAGE = "The automatic database backup failed. There are still pending migrations before you can launch.\nConsider manually making a backup before proceeding.";

    private final Flyway flyway;

    private boolean hasPendingMigrations = false;

    public FlywayMigrator(final FlywayConfig flywayConfig) {
        final String strippedUrl = flywayConfig.getProperty("flyway.url").replaceAll("jdbc:sqlite:", "");
        this.flyway = Flyway.configure()
                .configuration(flywayConfig.getProperties())
                .callbacks(new BackupCallback(strippedUrl))
                .dataSource(flywayConfig.getProperty("flyway.url"), null, null)
                .validateMigrationNaming(true)
                .baselineOnMigrate(true)
                .baselineVersion("1.0")
                .load();

        checkForPendingMigrations();
    }

    private void checkForPendingMigrations() {
        final MigrationInfo[] pendingMigrations = flyway.info().pending();
        this.hasPendingMigrations = pendingMigrations.length > 0;
    }

    public boolean isPendingMigration() {
        return hasPendingMigrations;
    }

    public void migrate() {
        flyway.migrate();
    }

    private static class BackupCallback implements Callback {

        private final String databaseUrl;

        public BackupCallback(final String databaseUrl) {
            this.databaseUrl = databaseUrl;
        }

        @Override public boolean supports(Event event, Context context) {
            return event == Event.BEFORE_EACH_MIGRATE;
        }

        @Override public boolean canHandleInTransaction(Event event, Context context) {
            return true;
        }

        @Override public void handle(Event event, Context context) {
            if (event == Event.BEFORE_EACH_MIGRATE) {
                final DatabaseBackup databaseBackup = new DatabaseBackup(new File(databaseUrl));
                if (!databaseBackup.backup()) {
                    final ButtonType migrate = new ButtonType("Migrate", ButtonBar.ButtonData.OK_DONE);
                    final ButtonType shutdown = new ButtonType("Shutdown", ButtonBar.ButtonData.NO);
                    AlertPopup.get().showWarningConfirmation(
                            "WARNING!", "Database Backup Failed", WARNING_MESSAGE, migrate, shutdown
                    ).ifPresent(type -> {
                        if (type == shutdown) {
                            handleShutdown(context);
                        }
                    });
                }
            }
        }

        private void handleShutdown(final Context context) {
            try {
                context.getConnection().close();
                QuickTicket.getInstance().shutdown();
            } catch (SQLException e) {
                AlertPopup.get().showException("Shutdown", e.fillInStackTrace());
            }
        }

        @Override public String getCallbackName() {
            return BackupCallback.class.getName();
        }
    }
}
