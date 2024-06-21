package io.jacobking.quickticket.gui.screen.impl;

import io.jacobking.quickticket.gui.controller.impl.MetricsController;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.screen.Screen;
import javafx.stage.Modality;

public class MetricsScreen extends Screen {
    public MetricsScreen() {
        super(Route.METRICS, Modality.APPLICATION_MODAL, new MetricsController());
    }
}
