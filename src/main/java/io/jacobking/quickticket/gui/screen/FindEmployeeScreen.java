package io.jacobking.quickticket.gui.screen;

import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.Screen;
import io.jacobking.quickticket.gui.controller.FindEmployeeController;
import javafx.stage.Modality;

public class FindEmployeeScreen extends Screen {
    public FindEmployeeScreen() {
        super(Route.FIND_EMPLOYEE, Modality.APPLICATION_MODAL, new FindEmployeeController());
    }
}
