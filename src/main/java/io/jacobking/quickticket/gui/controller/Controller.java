package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.bridge.impl.*;
import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.email.EmailConfig;
import io.jacobking.quickticket.gui.data.DataRelay;
import javafx.fxml.Initializable;

public abstract class Controller implements Initializable {

    protected final   QuickTicket         core;
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
        this.ticket = Database.getInstance().getBridgeContext().getTicket();
        this.comment = Database.getInstance().getBridgeContext().getComment();
        this.flyway = Database.getInstance().getBridgeContext().getFlyway();
        this.employee = Database.getInstance().getBridgeContext().getEmployee();
        this.email = Database.getInstance().getBridgeContext().getEmail();
        this.alertSettings = Database.getInstance().getBridgeContext().getAlertSettings();
        this.company = Database.getInstance().getBridgeContext().getCompany();
        this.department = Database.getInstance().getBridgeContext().getDepartment();
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
