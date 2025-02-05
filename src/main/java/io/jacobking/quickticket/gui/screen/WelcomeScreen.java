package io.jacobking.quickticket.gui.screen;

import io.jacobking.quickticket.gui.controller.WelcomeController;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.Screen;
import javafx.stage.Modality;

public class WelcomeScreen extends Screen {
    public WelcomeScreen() {
        super(Route.WELCOME, Modality.APPLICATION_MODAL, new WelcomeController());
    }
}
