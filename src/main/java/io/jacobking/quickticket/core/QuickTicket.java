package io.jacobking.quickticket.core;

import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;

public class QuickTicket {

    private QuickTicket() {
        final Display display = new Display();
        display.show(Route.DASHBOARD);
    }

    public static void launch() {
        new QuickTicket();
    }
}
