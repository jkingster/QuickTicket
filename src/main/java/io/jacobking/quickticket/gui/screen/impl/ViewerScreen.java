package io.jacobking.quickticket.gui.screen.impl;

import io.jacobking.quickticket.gui.controller.impl.ticket.ViewerController;
import io.jacobking.quickticket.gui.data.DataRelay;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.screen.Screen;
import javafx.stage.Modality;

public class ViewerScreen extends Screen {
    public ViewerScreen() {
        super(Route.VIEWER, Modality.APPLICATION_MODAL);
    }

    @Override
    public void display(DataRelay dataRelay) {
        if (dataRelay == null) {
            return;
        }


        setController(new ViewerController().setData(dataRelay));
        super.display(dataRelay);
    }
}
