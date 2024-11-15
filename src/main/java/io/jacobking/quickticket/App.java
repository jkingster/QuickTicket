package io.jacobking.quickticket;

import io.jacobking.quickticket.bridge.BridgeContext;
import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.utility.FileIO;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.alert.Announcements;
import javafx.application.Application;
import javafx.scene.control.ListCell;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

public class App extends Application {

    public static void main(String[] args) {
        System.setProperty("LOG_ROOT", FileIO.getPath("logs"));
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        FileIO.createAppDirectory();

        final QuickTicket quickTicket = QuickTicket.getInstance();
        final Database database = quickTicket.getDatabase();
        if (!database.hasConnection()) {
            throw new RuntimeException("FATAL: Could not establish database connection.");
        }



        final BridgeContext bridgeContext = quickTicket.getBridgeContext();
        Announcements.get().establishSettings(bridgeContext);

        final boolean fileLockingDisabled = quickTicket.getSystemConfig()
                .parseBoolean("disable_file_locking");

        if (fileLockingDisabled) {
            quickTicket.getDisplay().show(Route.DASHBOARD);
            return;
        }

        quickTicket.getInstanceLock().checkLock();
        if (quickTicket.getInstanceLock().isUnlocked()) {
            quickTicket.getDisplay().show(Route.DASHBOARD);
        }
    }

}
