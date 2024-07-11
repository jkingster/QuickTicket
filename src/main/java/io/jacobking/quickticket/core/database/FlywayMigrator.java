package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.core.config.FlywayConfig;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;

public class FlywayMigrator {


    private final Flyway flyway;

    private boolean hasPendingMigrations = false;

    public FlywayMigrator(final FlywayConfig flywayConfig) {
        this.flyway = Flyway.configure()
                .configuration(flywayConfig.getProperties())
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
}
