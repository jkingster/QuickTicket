package io.jacobking.quickticket.gui.screen.impl;

import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.controller.impl.AboutController;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.screen.Screen;
import javafx.stage.Modality;

public class AboutScreen extends Screen {
    public AboutScreen() {
        super(Route.ABOUT, Modality.APPLICATION_MODAL, new AboutController());
    }
}
