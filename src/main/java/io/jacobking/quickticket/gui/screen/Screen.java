package io.jacobking.quickticket.gui.screen;

import io.jacobking.quickticket.App;
import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.core.utility.Logs;
import io.jacobking.quickticket.gui.alert.builder.NotificationBuilder;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.data.DataRelay;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

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
            if (this.route != Route.DASHBOARD) {
                scene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ESCAPE) {
                        Display.close(route);
                    }
                });
            }
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
        stage.setScene(scene);
        stage.getScene().getStylesheets().add(0, NotificationBuilder.getStylesheet());
        if (this.route == Route.DASHBOARD) {
            stage.setMaximized(true);
            stage.setOnCloseRequest(event -> QuickTicket.getInstance().shutdown());
        }

        final Image imageIcon = getIconImage();
        if (imageIcon != null) {
            stage.getIcons().add(getIconImage());
        }

        stage.centerOnScreen();
    }

    private FXMLLoader setAndGetLoader() {
        final String path = route.getPath();
        this.loader = new FXMLLoader(App.class.getResource(path));
        return loader;
    }

    private Image getIconImage() {
        try (final InputStream stream = App.class.getResourceAsStream("icons/quickticket-icon2.png")) {
            if (stream != null) {
                return new Image(stream);
            }
        } catch (IOException e) {
            Logs.warn(e.getMessage());
        }
        return null;
    }


}
