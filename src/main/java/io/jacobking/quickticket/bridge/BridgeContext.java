package io.jacobking.quickticket.bridge;

import io.jacobking.quickticket.bridge.impl.*;
import io.jacobking.quickticket.core.database.Database;

public class BridgeContext {

    private final CompanyBridge       company;
    private final DepartmentBridge    department;
    private final EmployeeBridge      employee;
    private final TicketBridge        ticket;
    private final CommentBridge       comment;
    private final EmailBridge         email;
    private final AlertSettingsBridge alertSettings;
    private final FlywayBridge        flyway;

    public BridgeContext(final Database database) {
        this.company = new CompanyBridge(database);
        this.department = new DepartmentBridge(database);
        this.employee = new EmployeeBridge(database);
        this.ticket = new TicketBridge(database);
        this.comment = new CommentBridge(database);
        this.email = new EmailBridge(database);
        this.alertSettings = new AlertSettingsBridge(database);
        this.flyway = new FlywayBridge(database);
    }

    public CompanyBridge getCompany() {
        return company;
    }

    public DepartmentBridge getDepartment() {
        return department;
    }

    public EmployeeBridge getEmployee() {
        return employee;
    }

    public TicketBridge getTicket() {
        return ticket;
    }

    public CommentBridge getComment() {
        return comment;
    }

    public EmailBridge getEmail() {
        return email;
    }

    public AlertSettingsBridge getAlertSettings() {
        return alertSettings;
    }

    public FlywayBridge getFlyway() {
        return flyway;
    }
}
