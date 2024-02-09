package io.jacobking.quickticket.core.database.entity.impl;

import io.jacobking.quickticket.core.database.entity.Entity;
import io.jacobking.quickticket.core.type.PriorityType;

public class TicketEntity extends Entity  {

    private String title;
    private String description;

    private PriorityType priority;

    public TicketEntity(int id) {
        super(id);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PriorityType getPriority() {
        return priority;
    }

    public void setPriority(PriorityType priority) {
        this.priority = priority;
    }
}
