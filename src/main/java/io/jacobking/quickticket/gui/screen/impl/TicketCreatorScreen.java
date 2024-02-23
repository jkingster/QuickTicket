package io.jacobking.quickticket.gui.screen.impl;

import io.jacobking.quickticket.gui.controller.impl.ticket.TicketCreatorController;
import io.jacobking.quickticket.gui.data.DataRelay;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.screen.Screen;
import javafx.stage.Modality;

public class TicketCreatorScreen extends Screen {
    public TicketCreatorScreen() {
        super(Route.TICKET_CREATOR, Modality.APPLICATION_MODAL);
    }

    @Override
    public void display(DataRelay dataRelay) {
        if (dataRelay == null) {
            return;
        }

        setController(new TicketCreatorController().setData(dataRelay));
        super.display(dataRelay);
    }
}
