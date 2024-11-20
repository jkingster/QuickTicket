package io.jacobking.quickticket.gui.model;

import io.jacobking.quickticket.gui.Model;
import io.jacobking.quickticket.tables.pojos.TicketLink;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LinkModel extends Model<TicketLink> {

    private final IntegerProperty ticketId    = new SimpleIntegerProperty();
    private final StringProperty  link        = new SimpleStringProperty();
    private final StringProperty  description = new SimpleStringProperty();
    private final StringProperty  extension   = new SimpleStringProperty();

    public LinkModel(int id, int ticketId, String link, String extension, String description) {
        super(id);
        this.ticketId.setValue(ticketId);
        this.link.setValue(link);
        this.description.setValue(description);
        this.extension.setValue(extension);
    }

    public LinkModel(final TicketLink link) {
        this(
                link.getId(),
                link.getTicketId(),
                link.getLink(),
                link.getDescription(),
                link.getExtension()
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

    public String getLink() {
        return link.get();
    }

    public StringProperty linkProperty() {
        return link;
    }

    public void setLink(String link) {
        this.link.set(link);
    }

    public String getDescription() {
        return description.get() == null ? "No description provided." : description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getExtension() {
        return extension.get();
    }

    public StringProperty extensionProperty() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension.set(extension);
    }

    @Override public TicketLink toEntity() {
        System.out.println(getDescription());
        return new TicketLink()
                .setId(getId())
                .setTicketId(getTicketId())
                .setLink(getLink())
                .setDescription(getDescription())
                .setExtension(getExtension());
    }
}
