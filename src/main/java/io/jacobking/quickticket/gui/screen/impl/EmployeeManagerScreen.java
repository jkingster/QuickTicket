package io.jacobking.quickticket.gui.screen.impl;

import io.jacobking.quickticket.gui.controller.impl.EmployeeManagerController;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.screen.Screen;
import javafx.stage.Modality;

public class EmployeeManagerScreen extends Screen {
    public EmployeeManagerScreen() {
        super(Route.EMPLOYEE_MANAGER, Modality.APPLICATION_MODAL, new EmployeeManagerController());
    }
}
