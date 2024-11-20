package io.jacobking.quickticket.gui.screen;

import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.Screen;
import io.jacobking.quickticket.gui.controller.AddURLController;
import javafx.stage.Modality;

public class AddURLScreen extends Screen {
    public AddURLScreen() {
        super(Route.ADD_URL, Modality.APPLICATION_MODAL, new AddURLController());

    }
}
