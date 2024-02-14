package io.jacobking.quickticket.core;

import io.jacobking.quickticket.core.database.Database;

public class QuickTicket {

    private final Config config;
    private final Database database;

    private QuickTicket() {
        this.config = Config.getInstance();
        this.database = new Database();
        //Display.show(Route.DASHBOARD, null);
    }

    public static void launch() {
        new QuickTicket();
    }
}
