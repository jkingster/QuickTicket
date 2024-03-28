package io.jacobking.quickticket;

import io.jacobking.quickticket.core.config.impl.FlywayConfig;
import io.jacobking.quickticket.core.config.impl.SystemConfig;
import io.jacobking.quickticket.core.lock.InstanceLock;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        SystemConfig.getInstance();
        FlywayConfig.getInstance();
        InstanceLock.getInstance();

    }
}
