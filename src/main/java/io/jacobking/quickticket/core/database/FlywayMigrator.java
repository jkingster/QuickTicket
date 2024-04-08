package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.core.config.impl.FlywayConfig;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;

public class FlywayMigrator {

    private static final String DB_URL = FlywayConfig.getInstance().getProperty("flyway.url");

    private final Flyway flyway;

    private boolean hasPendingMigrations = false;

    private FlywayMigrator() {
        this.flyway = Flyway.configure()
                .configuration(FlywayConfig.getInstance().getProperties())
                .dataSource(DB_URL, null, null)
                .validateMigrationNaming(true)
                .baselineOnMigrate(true)
                .baselineVersion("base")
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

    public static FlywayMigrator init() {
        return new FlywayMigrator();
    }

    public void migrate() {
        flyway.migrate();
    }
}
