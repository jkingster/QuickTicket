package io.jacobking.quickticket.gui.screen;

import io.jacobking.quickticket.gui.controller.CategoryCreatorController;
import io.jacobking.quickticket.gui.Data;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.Screen;
import javafx.stage.Modality;

public class CategoryCreatorScreen extends Screen {
    public CategoryCreatorScreen() {
        super(Route.CATEGORY_CREATOR, Modality.APPLICATION_MODAL);
    }

}
