package io.jacobking.quickticket.gui.screen.impl;

import io.jacobking.quickticket.gui.controller.impl.DashboardController;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.screen.Screen;
import javafx.stage.Modality;

public class DashboardScreen extends Screen {
    public DashboardScreen() {
        super(Route.DASHBOARD, Modality.NONE, new DashboardController());
    }



}
