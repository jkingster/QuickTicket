package io.jacobking.quickticket.gui.screen.impl;

import io.jacobking.quickticket.gui.controller.impl.DatabaseController;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.screen.Screen;
import javafx.stage.Modality;

public class DatabaseScreen extends Screen {
    public DatabaseScreen() {
        super(Route.DATABASE, Modality.APPLICATION_MODAL, new DatabaseController());
    }
}
