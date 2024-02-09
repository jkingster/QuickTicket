package io.jacobking.quickticket.core;

import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;

public class QuickTicket {
    private QuickTicket() {
        Display.show(Route.DASHBOARD, null);
    }

    public static void launch() {
        new QuickTicket();
    }
}
