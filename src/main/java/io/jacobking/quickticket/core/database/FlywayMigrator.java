package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.App;
import io.jacobking.quickticket.core.config.impl.FlywayConfig;
import org.flywaydb.core.Flyway;

public class FlywayMigrator {

    private static final String DB_URL = FlywayConfig.getInstance().getProperty("flyway.url");

    private final Flyway flyway;

    private FlywayMigrator() {
        this.flyway = Flyway.configure()
                .configuration(FlywayConfig.getInstance().getProperties())
                .dataSource(DB_URL, null, null)
                .validateMigrationNaming(true)
                .baselineOnMigrate(true)
                .baselineVersion("1.1")
                .load();
    }

    public static void migrate() {
        new FlywayMigrator().flyway.migrate();
    }
}
