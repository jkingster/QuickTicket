package io.jacobking.quickticket.gui.screen;

import io.jacobking.quickticket.gui.controller.AboutController;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.Screen;
import javafx.stage.Modality;

public class AboutScreen extends Screen {
    public AboutScreen() {
        super(Route.ABOUT, Modality.APPLICATION_MODAL, new AboutController());
    }
}
