package io.jacobking.quickticket.gui.screen.impl;

import io.jacobking.quickticket.gui.controller.impl.ChangelogController;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.screen.Screen;
import javafx.stage.Modality;

public class ChangelogScreen extends Screen {
    public ChangelogScreen() {
        super(Route.CHANGELOG, Modality.APPLICATION_MODAL, new ChangelogController());
    }
}
