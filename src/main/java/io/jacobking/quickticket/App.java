package io.jacobking.quickticket;

import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.core.utility.ChangelogReader;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
       QuickTicket.launch();
    }
}
