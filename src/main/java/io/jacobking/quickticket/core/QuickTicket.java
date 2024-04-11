package io.jacobking.quickticket.core;

import io.jacobking.quickticket.core.config.FlywayConfig;
import io.jacobking.quickticket.core.config.SystemConfig;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.utility.Logs;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class QuickTicket {

    private static       QuickTicket instance;
    private static final Executor    EXECUTOR = Executors.newFixedThreadPool(3);

    private final InstanceLock instanceLock;
    private final SystemConfig systemConfig;
    private final FlywayConfig flywayConfig;
    private final Database     database;

    private QuickTicket() {
        Logs.info("Initializing QuickTicket...");
        this.systemConfig = new SystemConfig();
        this.flywayConfig = new FlywayConfig();
        this.database = new Database(systemConfig, flywayConfig);
        this.instanceLock = InstanceLock.getInstance();
    }

    public static synchronized QuickTicket getInstance() {
        if (instance == null) {
            instance = new QuickTicket();
        }
        return instance;
    }

    public static void execute(final Runnable runnable) {
        EXECUTOR.execute(runnable);
    }

    public void shutdown() {

    }

    public InstanceLock getLock() {
        return instanceLock;
    }

    public SystemConfig getSystemConfig() {
        return systemConfig;
    }

    public FlywayConfig getFlywayConfig() {
        return flywayConfig;
    }


    public Database getDatabase() {
        return database;
    }
}
