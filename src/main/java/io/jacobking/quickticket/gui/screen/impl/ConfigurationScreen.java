package io.jacobking.quickticket.gui.screen.impl;

import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.gui.controller.impl.ConfigurationController;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.screen.Screen;
import javafx.stage.Modality;

public class ConfigurationScreen extends Screen {
    public ConfigurationScreen() {
        super(Route.CONFIGURATION, Modality.APPLICATION_MODAL, new ConfigurationController());
    }
}
