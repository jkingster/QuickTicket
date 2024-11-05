package io.jacobking.quickticket.core;

import io.jacobking.quickticket.bridge.BridgeContext;
import io.jacobking.quickticket.core.config.SystemConfig;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.gui.Display;
import javafx.application.Platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuickTicket {

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(3);

    private static QuickTicket instance;

    private final InstanceLock  instanceLock;
    private final SystemConfig  systemConfig;
    private final Database      database;
    private final BridgeContext bridgeContext;
    private final Display       display;

    private QuickTicket() {
        this.instanceLock = InstanceLock.getInstance();
        this.systemConfig = new SystemConfig();
        this.database = new Database(systemConfig);
        this.bridgeContext = new BridgeContext(database);
        this.display = new Display();
    }

    public static synchronized QuickTicket getInstance() {
        if (instance == null) {
            instance = new QuickTicket();
        }
        return instance;
    }

    public boolean isReady() {
        return (database != null) && (bridgeContext != null) && (display != null);
    }

    public void shutdown() {
        Platform.runLater(() -> {
            EXECUTOR.shutdown();
            database.close();
            instanceLock.deleteLock();
            Platform.exit();
        });
    }

    public InstanceLock getInstanceLock() {
        return instanceLock;
    }

    public SystemConfig getSystemConfig() {
        return systemConfig;
    }

    public Database getDatabase() {
        return database;
    }

    public Display getDisplay() {
        return display;
    }

    public BridgeContext getBridgeContext() {
        return bridgeContext;
    }
}
