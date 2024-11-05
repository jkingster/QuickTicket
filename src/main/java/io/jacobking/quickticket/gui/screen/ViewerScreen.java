package io.jacobking.quickticket.gui.screen;

import io.jacobking.quickticket.gui.controller.ViewerController;
import io.jacobking.quickticket.gui.Data;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.Screen;
import javafx.stage.Modality;

public class ViewerScreen extends Screen {
    public ViewerScreen() {
        super(Route.VIEWER, Modality.APPLICATION_MODAL);
    }

}
