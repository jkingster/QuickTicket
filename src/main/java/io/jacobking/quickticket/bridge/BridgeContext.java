package io.jacobking.quickticket.bridge;

import io.jacobking.quickticket.bridge.impl.*;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.email.EmailConfig;

public class BridgeContext {

    private final CompanyBridge       company;
    private final DepartmentBridge    department;
    private final EmployeeBridge      employee;
    private final TicketBridge        ticket;
    private final CommentBridge       comment;
    private final EmailBridge         email;
    private final FlywayBridge        flyway;
    private final LinkedTicketBridge  linkedTicket;
    private final EmailConfig         emailConfig;
    private final CategoryBridge      categoryBridge;
    private final InventoryBridge     inventory;
    private final InventoryLogBridge  inventoryLog;

    public BridgeContext(final Database database) {
        this.company = new CompanyBridge(database);
        this.department = new DepartmentBridge(database);
        this.employee = new EmployeeBridge(database);
        this.ticket = new TicketBridge(database);
        this.comment = new CommentBridge(database);
        this.email = new EmailBridge(database);
        this.flyway = new FlywayBridge(database);
        this.linkedTicket = new LinkedTicketBridge(database);
        this.emailConfig = new EmailConfig();
        this.categoryBridge = new CategoryBridge(database);
        this.inventory = new InventoryBridge(database);
        this.inventoryLog = new InventoryLogBridge(database);
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


    public FlywayBridge getFlyway() {
        return flyway;
    }

    public LinkedTicketBridge getLinkedTicket() {
        return linkedTicket;
    }

    public EmailConfig getEmailConfig() {
        return emailConfig;
    }

    public CategoryBridge getCategoryBridge() {
        return categoryBridge;
    }

    public InventoryBridge getInventory() {
        return inventory;
    }

    public InventoryLogBridge getInventoryLog() {
        return inventoryLog;
    }
}
