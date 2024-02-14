package io.jacobking.quickticket.gui.model.impl;


import io.jacobking.quickticket.gui.model.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.jooq.User;

public class CommentModel extends ViewModel<User> {

    private final StringProperty commentProperty = new SimpleStringProperty();
    private final StringProperty commentedOnProperty = new SimpleStringProperty();

    public CommentModel(int id, String comment, String commentedOn) {
        super(id);
        this.commentProperty.setValue(comment);
        this.commentedOnProperty.setValue(commentedOn);
    }

    public String getComment() {
        return commentProperty.getValueSafe();
    }

    public String getCommentDate() {
        return commentedOnProperty.getValueSafe();
    }

    public StringProperty commentProperty() {
        return commentProperty;
    }

    public StringProperty commentedOnProperty() {
        return commentedOnProperty;
    }


}
