package io.jacobking.quickticket.core;

import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;
import javafx.application.Platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuickTicket {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(5);

    private final Config   config;
    private final Database database;

    private QuickTicket() {
        this.config = Config.getInstance();
        this.database = Database.getInstance();
        Display.show(Route.DASHBOARD);
    }

    public static void execute(final Runnable runnable) {
        EXECUTOR_SERVICE.execute(runnable);
    }

    public static void launch() {
        new QuickTicket();
    }

    public static void shutdown() {
        Database.getInstance().close();
        EXECUTOR_SERVICE.shutdown();
        Platform.exit();
    }
}
