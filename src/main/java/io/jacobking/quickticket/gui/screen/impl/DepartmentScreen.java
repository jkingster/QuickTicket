package io.jacobking.quickticket.gui.screen.impl;

import io.jacobking.quickticket.gui.controller.impl.DepartmentController;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.screen.Screen;
import javafx.stage.Modality;

public class DepartmentScreen extends Screen {
    public DepartmentScreen() {
        super(Route.DEPARTMENT, Modality.APPLICATION_MODAL, new DepartmentController());
    }
}
