package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.bridge.BridgeContext;
import io.jacobking.quickticket.bridge.impl.TicketBridge;
import io.jacobking.quickticket.gui.data.DataRelay;
import javafx.fxml.Initializable;

public abstract class Controller implements Initializable {

    protected final TicketBridge ticket;
    protected DataRelay dataRelay;

    public Controller() {
        this.ticket = BridgeContext.ticket();
        this.dataRelay = null;
    }

    public Controller setData(final DataRelay dataRelay) {
        this.dataRelay = dataRelay;
        return this;
    }

}
