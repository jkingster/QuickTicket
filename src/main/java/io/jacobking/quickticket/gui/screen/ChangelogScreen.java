package io.jacobking.quickticket.gui.screen;

import io.jacobking.quickticket.gui.controller.ChangelogController;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.Screen;
import javafx.stage.Modality;

public class ChangelogScreen extends Screen {
    public ChangelogScreen() {
        super(Route.CHANGELOG, Modality.APPLICATION_MODAL, new ChangelogController());
    }
}
