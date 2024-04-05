package io.jacobking.quickticket.core.reload;

import io.jacobking.quickticket.bridge.BridgeContext;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;

public class ReloadMechanism {

    private ReloadMechanism() {

    }

    private void start() {
        Display.closeAll();
        Database.rebuildInstance();
        BridgeContext.rebuildInstance();
        Display.show(Route.DASHBOARD);
    }

    public static void reload() {
        new ReloadMechanism().start();
    }

}
