package io.jacobking.quickticket.gui.screen.impl;

import io.jacobking.quickticket.gui.controller.impl.CategoryCreatorController;
import io.jacobking.quickticket.gui.controller.impl.ticket.ViewerController;
import io.jacobking.quickticket.gui.data.DataRelay;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.screen.Screen;
import javafx.stage.Modality;

public class CategoryCreatorScreen extends Screen {
    public CategoryCreatorScreen() {
        super(Route.CATEGORY_CREATOR, Modality.APPLICATION_MODAL);
    }

    @Override public void display(DataRelay dataRelay) {
        if (dataRelay == null) {
            return;
        }


        setController(new CategoryCreatorController().setData(dataRelay));
        super.display(dataRelay);
    }
}
