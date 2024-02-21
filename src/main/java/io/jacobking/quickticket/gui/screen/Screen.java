package io.jacobking.quickticket.gui.screen;

import io.jacobking.quickticket.App;
import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.data.DataRelay;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Screen {

    private static final String     TITLE = "QuickTicket";
    private final        Route      route;
    private final        Modality   modality;
    private              FXMLLoader loader;
    private              Stage      stage;
    private              Scene      scene;
    private              Controller controller;

    public Screen(Route route, Modality modality, Controller controller) {
        this.route = route;
        this.modality = modality;
        this.controller = controller;
    }

    public Screen(Route route, Modality modality) {
        this(route, modality, null);
    }

    public void display(final DataRelay dataRelay) {
        configure();
        show();
    }

    public void close() {
        if (this.stage != null) {
            stage.hide();
        }
    }

    public void initializeController() {
        if (this.controller != null) {
            setController(controller);
        }
    }

    public void setController(final Controller controller) {
        if (controller == null) {
            return;
        }

        final FXMLLoader loader = setAndGetLoader();
        this.controller = controller;
        loader.setController(controller);
    }

    private void configure() {
        initializeController();
        initializeScene();

        if (scene != null) {
            initializeStageDefaults();
        }
    }

    private void initializeScene() {
        try {
            this.scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void show() {
        if (this.stage != null) {
            stage.show();
        }
    }

    private void initializeStageDefaults() {
        this.stage = new Stage();
        stage.initModality(modality);
        stage.setTitle(TITLE);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setScene(scene);

        if (route == Route.DASHBOARD) {
            stage.setOnCloseRequest(event -> QuickTicket.shutdown());
        }
    }

    private FXMLLoader setAndGetLoader() {
        final String path = route.getPath();
        this.loader = new FXMLLoader(App.class.getResource(path));
        return loader;
    }


}
