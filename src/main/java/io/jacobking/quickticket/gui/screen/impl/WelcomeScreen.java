package io.jacobking.quickticket.gui.screen.impl;

import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.controller.impl.WelcomeController;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.screen.Screen;
import javafx.stage.Modality;

public class WelcomeScreen extends Screen {
    public WelcomeScreen() {
        super(Route.WELCOME, Modality.APPLICATION_MODAL, new WelcomeController());
    }
}
