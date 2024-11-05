package io.jacobking.quickticket.gui;

import io.jacobking.quickticket.bridge.BridgeContext;
import io.jacobking.quickticket.core.QuickTicket;
import javafx.fxml.Initializable;

public abstract class Controller implements Initializable {

    protected Display       display;
    protected BridgeContext bridgeContext;
    protected Data          data;

    public void controllerInitialization() {
        this.display = QuickTicket.getInstance().getDisplay();
        this.bridgeContext = QuickTicket.getInstance().getBridgeContext();
    }

    public void setData(final Data data) {
        this.data = data;
    }

}
