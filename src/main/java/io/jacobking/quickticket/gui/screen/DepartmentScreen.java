package io.jacobking.quickticket.gui.screen;

import io.jacobking.quickticket.gui.controller.DepartmentController;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.Screen;
import javafx.stage.Modality;

public class DepartmentScreen extends Screen {
    public DepartmentScreen() {
        super(Route.DEPARTMENT, Modality.APPLICATION_MODAL, new DepartmentController());
    }
}
