package io.jacobking.quickticket.gui.model.impl;

import io.jacobking.quickticket.gui.model.ViewModel;
import io.jacobking.quickticket.tables.pojos.Journal;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class JournalModel extends ViewModel<Journal> {

    private final StringProperty  noteProperty           = new SimpleStringProperty();
    private final StringProperty  createdOnProperty      = new SimpleStringProperty();

    public JournalModel(int id, String note, String createdOn) {
        super(id);
        this.noteProperty.setValue(note);
        this.createdOnProperty.setValue(createdOn);
    }

    public JournalModel(final Journal journal) {
        this(
                journal.getId(),
                journal.getNote(),
                journal.getCreatedOn()
        );
    }

    public String getNoteProperty() {
        return noteProperty.getValueSafe();
    }

    public StringProperty noteProperty() {
        return noteProperty;
    }

    public void setNoteProperty(String noteProperty) {
        this.noteProperty.set(noteProperty);
    }

    public String getCreatedOnProperty() {
        return createdOnProperty.getValueSafe();
    }

    public StringProperty createdOnProperty() {
        return createdOnProperty;
    }

    public void setCreatedOnProperty(String createdOnProperty) {
        this.createdOnProperty.set(createdOnProperty);
    }


    @Override public Journal toEntity() {
        return new Journal()
                .setId(getId())
                .setNote(getNoteProperty())
                .setCreatedOn(getCreatedOnProperty());
    }
}
