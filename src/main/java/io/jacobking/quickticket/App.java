package io.jacobking.quickticket;

import io.jacobking.quickticket.bridge.BridgeContext;
import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.utility.FileIO;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.Display;
import io.jacobking.quickticket.gui.Route;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        System.setProperty("LOG_ROOT", FileIO.getPath("logs"));
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        FileIO.createAppDirectory();

        final QuickTicket quickTicket = QuickTicket.getInstance();
        quickTicket.initializeDatabase();

        final Database database = quickTicket.getDatabase();
        if (!database.hasConnection()) {
            throw new RuntimeException("FATAL: Could not establish database connection.");
        }

        database.checkForMigration();
        database.initializeBridgeContext();

        final BridgeContext bridgeContext = database.getBridgeContext();
        Announcements.get().establishSettings(bridgeContext);

        final boolean fileLockingDisabled = quickTicket.getSystemConfig()
                .parseBoolean("disable_file_locking");

        if (fileLockingDisabled) {
            Display.show(Route.DASHBOARD);
            return;
        }

        quickTicket.getLock().checkLock();
        if (quickTicket.getLock().isUnlocked()) {
            Display.show(Route.DASHBOARD);
        }
    }

}
