package io.jacobking.quickticket;

import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.core.utility.FileIO;
import io.jacobking.quickticket.core.utility.Logs;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        System.setProperty("LOG_ROOT", FileIO.getPath("logs"));
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Logs.getInstance();
        FileIO.createAppDirectory();

        final QuickTicket quickTicket = QuickTicket.getInstance();
        if (quickTicket.getDatabase().isConfigured()) {
            quickTicket.getLock().checkLock();
            if (quickTicket.getLock().isUnlocked()) {
                Display.show(Route.DASHBOARD);
            }
        }

    }
}
