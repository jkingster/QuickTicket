package io.jacobking.quickticket.gui.model.impl;

import io.jacobking.quickticket.core.database.entity.impl.CommentEntity;
import io.jacobking.quickticket.gui.model.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CommentModel extends ViewModel<CommentEntity> {

    private final StringProperty commentProperty = new SimpleStringProperty();
    private final StringProperty commentedOnProperty = new SimpleStringProperty();

    public CommentModel(int id, String comment, String commentedOn) {
        super(id);
        this.commentProperty.setValue(comment);
        this.commentedOnProperty.setValue(commentedOn);
    }

    public StringProperty commentProperty() {
        return commentProperty;
    }

    public StringProperty commentedOnProperty() {
        return commentedOnProperty;
    }


}
