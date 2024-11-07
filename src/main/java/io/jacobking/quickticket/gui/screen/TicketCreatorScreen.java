package io.jacobking.quickticket.gui.screen;

import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.Screen;
import io.jacobking.quickticket.gui.controller.TicketCreatorController;
import javafx.stage.Modality;

public class TicketCreatorScreen extends Screen {
    public TicketCreatorScreen() {
        super(Route.TICKET_CREATOR, Modality.APPLICATION_MODAL, new TicketCreatorController());
    }

}
