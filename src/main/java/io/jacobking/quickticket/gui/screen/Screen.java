package io.jacobking.quickticket.gui.screen;

import io.jacobking.quickticket.App;
import io.jacobking.quickticket.gui.controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Screen {
    private static final String TITLE = "QuickTicket";
    private final Display display;
    private final Route route;
    private final Modality modality;
    private final Controller baseController;
    private final FXMLLoader loader;

    private Stage stage;

    public Screen(final Display display, final Route route, final Modality modality, final Controller baseController) {
        this.display = display;
        this.route = route;
        this.modality = modality;
        this.baseController = baseController;
        this.loader = getLoader();
        initializeController();
    }

    public void show() {
        if (stage != null) {
            stage.show();
        }
    }

    public void close() {
        if (stage != null) {
            stage.hide();
        }
    }

    public void setStage(final Stage stage) {
        this.stage = stage;
        initializeStageDefaults();
    }

    private void initializeController() {
        if (baseController != null) {
            baseController.setDisplay(display);
            loader.setController(baseController);
        }
    }

    private void initializeStageDefaults() {
        if (stage == null) {
            return;
        }
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.initModality(modality);
        stage.setTitle(TITLE);

        loadScene();
    }

    private void loadScene() {
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FXMLLoader getLoader() {
        final String path = route.getPath();
        return new FXMLLoader(App.class.getResource(path));
    }
}
