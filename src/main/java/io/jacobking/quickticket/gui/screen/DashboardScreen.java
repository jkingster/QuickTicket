package io.jacobking.quickticket.gui.screen;

import io.jacobking.quickticket.gui.controller.DashboardController;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.Screen;
import javafx.stage.Modality;

public class DashboardScreen extends Screen {
    public DashboardScreen() {
        super(Route.DASHBOARD, Modality.NONE, new DashboardController());
    }



}
