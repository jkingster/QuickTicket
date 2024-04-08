package io.jacobking.quickticket.gui.screen.impl;

import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.gui.controller.impl.SettingsController;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.screen.Screen;
import javafx.stage.Modality;

public class SettingsScreen extends Screen {
    public SettingsScreen() {
        super(Route.SETTINGS, Modality.APPLICATION_MODAL, new SettingsController());
    }
}
