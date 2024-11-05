package io.jacobking.quickticket.gui.screen;

import io.jacobking.quickticket.gui.controller.CompanyController;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.Screen;
import javafx.stage.Modality;

public class CompanyScreen extends Screen {
    public CompanyScreen() {
        super(Route.COMPANY, Modality.APPLICATION_MODAL, new CompanyController());
    }
}
