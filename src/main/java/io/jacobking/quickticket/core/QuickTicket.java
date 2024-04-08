package io.jacobking.quickticket.core;

import io.jacobking.quickticket.bridge.BridgeContext;
import io.jacobking.quickticket.core.config.impl.FlywayConfig;
import io.jacobking.quickticket.core.config.impl.SystemConfig;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class QuickTicket {

    private static       QuickTicket instance = null;
    private static final Executor    EXECUTOR = Executors.newFixedThreadPool(3);

    private final SystemConfig  systemConfig;
    private final FlywayConfig  flywayConfig;

    private QuickTicket() {
        this.systemConfig = new SystemConfig();
        this.flywayConfig = new FlywayConfig();
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

    public void checkLock() {
        final InstanceLock instanceLock = new InstanceLock();
        if (instanceLock.isUnlocked()) {
            Display.getInstance();
            Display.show(Route.DASHBOARD);
        }
    }

    public SystemConfig getSystemConfig() {
        return systemConfig;
    }

    public FlywayConfig getFlywayConfig() {
        return flywayConfig;
    }


    public void shutdown() {
        // do something()
    }
}
