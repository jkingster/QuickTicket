package io.jacobking.quickticket.core;

import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;

public class QuickTicket {

    private final Config   config;
    private final Database database;

    private QuickTicket() {
        this.config = Config.getInstance();
        this.database = Database.getInstance();
        Display.show(Route.DASHBOARD, null);
    }

    public static void launch() {
        new QuickTicket();
    }
}
