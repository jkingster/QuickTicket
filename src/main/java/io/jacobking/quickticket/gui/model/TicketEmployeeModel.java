package io.jacobking.quickticket.gui.model;

import io.jacobking.quickticket.gui.Model;
import io.jacobking.quickticket.tables.pojos.TicketEmployee;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class TicketEmployeeModel extends Model<TicketEmployee> {

    private final IntegerProperty ticketId   = new SimpleIntegerProperty();
    private final IntegerProperty employeeId = new SimpleIntegerProperty();

    public TicketEmployeeModel(int id, int ticketId, int employeeId) {
        super(id);
        this.ticketId.setValue(ticketId);
        this.employeeId.setValue(employeeId);
    }

    public TicketEmployeeModel(TicketEmployee ticketEmployee) {
        this(
                ticketEmployee.getId(),
                ticketEmployee.getTicketId(),
                ticketEmployee.getEmployeeId()
        );
    }

    public int getTicketId() {
        return ticketId.get();
    }

    public IntegerProperty ticketIdProperty() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId.set(ticketId);
    }

    public int getEmployeeId() {
        return employeeId.get();
    }

    public IntegerProperty employeeIdProperty() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId.set(employeeId);
    }

    @Override public TicketEmployee toEntity() {
        return new TicketEmployee()
                .setId(getId())
                .setTicketId(getTicketId())
                .setEmployeeId(getEmployeeId());
    }
}
