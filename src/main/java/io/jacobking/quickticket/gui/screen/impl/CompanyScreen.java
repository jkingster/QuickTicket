package io.jacobking.quickticket.gui.screen.impl;

import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.gui.controller.impl.CompanyController;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.screen.Screen;
import javafx.stage.Modality;

public class CompanyScreen extends Screen {
    public CompanyScreen() {
        super(Route.COMPANY, Modality.APPLICATION_MODAL, new CompanyController());
    }
}
