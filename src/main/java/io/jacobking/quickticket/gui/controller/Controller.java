package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;
import javafx.fxml.Initializable;

public abstract class Controller implements Initializable {

    private Display display;

    public void setDisplay(final Display display) {
        this.display = display;
    }

    public void show(final Route route) {
        if (display == null) {
            return;
        }
        display.show(route);
    }

    public void close(final Route route) {
        if (display == null) {
            return;
        }
        display.close(route);
    }

}
