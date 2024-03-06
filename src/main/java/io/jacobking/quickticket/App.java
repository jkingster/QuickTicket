package io.jacobking.quickticket;

import io.jacobking.quickticket.core.Config;
import io.jacobking.quickticket.core.lock.InstanceLock;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Config.getInstance();
        InstanceLock.getInstance();
    }
}
