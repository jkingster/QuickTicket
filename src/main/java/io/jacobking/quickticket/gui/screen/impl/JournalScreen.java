package io.jacobking.quickticket.gui.screen.impl;

import io.jacobking.quickticket.gui.controller.impl.journal.JournalController;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.screen.Screen;
import javafx.stage.Modality;

public class JournalScreen extends Screen {
    public JournalScreen() {
        super(Route.JOURNAL, Modality.APPLICATION_MODAL, new JournalController());
    }
}
