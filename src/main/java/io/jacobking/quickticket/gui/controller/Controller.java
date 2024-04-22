package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.bridge.impl.*;
import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.core.email.EmailConfig;
import io.jacobking.quickticket.gui.data.DataRelay;
import javafx.fxml.Initializable;

public abstract class Controller implements Initializable {

    protected final QuickTicket         core;
    protected final TicketBridge        ticket;
    protected final CommentBridge       comment;
    protected final EmployeeBridge      employee;
    protected final EmailBridge         email;
    protected final EmailConfig         emailConfig;
    protected final AlertSettingsBridge alertSettings;
    protected final FlywayBridge        flyway;
    protected final CompanyBridge       company;
    protected final DepartmentBridge    department;
    protected       DataRelay           dataRelay;

    public Controller() {
        this.core = QuickTicket.getInstance();
        this.ticket = core.getDatabase().getBridgeContext().getTicket();
        this.comment = core.getDatabase().getBridgeContext().getComment();
        this.flyway = core.getDatabase().getBridgeContext().getFlyway();
        this.employee = core.getDatabase().getBridgeContext().getEmployee();
        this.email = core.getDatabase().getBridgeContext().getEmail();
        this.alertSettings = core.getDatabase().getBridgeContext().getAlertSettings();
        this.company = core.getDatabase().getBridgeContext().getCompany();
        this.department = core.getDatabase().getBridgeContext().getDepartment();
        this.emailConfig = core.getDatabase().getBridgeContext().getEmailConfig()
                .setEmail(email.getEmail())
                .applySettings();
        this.dataRelay = DataRelay.empty();
    }

    public Controller setData(final DataRelay dataRelay) {
        this.dataRelay = dataRelay;
        return this;
    }

}
