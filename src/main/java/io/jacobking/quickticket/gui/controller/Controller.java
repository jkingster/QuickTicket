package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.bridge.BridgeContext;
import io.jacobking.quickticket.bridge.impl.*;
import io.jacobking.quickticket.core.email.EmailConfig;
import io.jacobking.quickticket.gui.data.DataRelay;
import javafx.fxml.Initializable;

public abstract class Controller implements Initializable {

    protected final TicketBridge  ticket;
    protected final CommentBridge comment;

    protected final EmployeeBridge employee;
    protected final EmailBridge    email;

    protected final EmailConfig emailConfig;
    protected final JournalBridge journal;
    protected final AlertSettingsBridge alertSettings;

    protected DataRelay dataRelay;

    public Controller() {
        this.ticket = BridgeContext.ticket();
        this.comment = BridgeContext.comment();
        this.employee = BridgeContext.employee();
        this.email = BridgeContext.email();
        this.journal = BridgeContext.journal();
        this.alertSettings = BridgeContext.alertSettings();
        this.emailConfig = EmailConfig.getInstance()
                .setEmail(email.getEmail())
                .applySettings();
        this.dataRelay = DataRelay.empty();
    }

    public Controller setData(final DataRelay dataRelay) {
        this.dataRelay = dataRelay;
        return this;
    }

}
