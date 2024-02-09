package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.gui.data.DataRelay;
import javafx.fxml.Initializable;

public abstract class Controller implements Initializable {
    protected DataRelay dataRelay;
    public Controller() {
        this.dataRelay = null;
    }

    public Controller setData(final DataRelay dataRelay) {
        this.dataRelay = dataRelay;
        return this;
    }

}
