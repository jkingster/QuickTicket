package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.bridge.impl.*;
import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.gui.data.Data;
import javafx.fxml.Initializable;

public abstract class Controller implements Initializable {

    protected final QuickTicket         core;
    protected final TicketBridge        ticket;
    protected final AlertBridge alerts;
    protected final CommentBridge       comment;
    protected final EmployeeBridge      employee;

    protected final CompanyBridge       company;
    protected final DepartmentBridge    department;
    protected final CategoryBridge category;
    protected       Data           data;


    public Controller() {
        this.core = QuickTicket.getInstance();
        this.ticket = core.getDatabase().getBridgeContext().getTicket();
        this.comment = core.getDatabase().getBridgeContext().getComment();
        this.alerts = core.getDatabase().getBridgeContext().getAlerts();
        this.employee = core.getDatabase().getBridgeContext().getEmployee();
        this.company = core.getDatabase().getBridgeContext().getCompany();
        this.department = core.getDatabase().getBridgeContext().getDepartment();
        this.category = core.getDatabase().getBridgeContext().getCategoryBridge();

        this.data = Data.empty();

    }

    public Controller setData(final Data data) {
        this.data = data;
        return this;
    }

}
