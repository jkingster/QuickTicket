package io.jacobking.quickticket.gui.model.impl;


import io.jacobking.quickticket.gui.model.ViewModel;
import io.jacobking.quickticket.tables.pojos.Comment;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CommentModel extends ViewModel<Comment> {


    private final int            ticketId;
    private final StringProperty postProperty     = new SimpleStringProperty();
    private final StringProperty postedOnProperty = new SimpleStringProperty();

    public CommentModel(final int id, final int ticketId, final String post, final String postedOn) {
        super(id);
        this.ticketId = ticketId;
        this.postProperty.setValue(post);
        this.postedOnProperty.setValue(postedOn);
    }

    public CommentModel(final Comment comment) {
        this(
                comment.getId(),
                comment.getTicketId(),
                comment.getPost(),
                comment.getPostedOn()
        );
    }

    public int getTicketId() {
        return ticketId;
    }

    public String getPost() {
        return postProperty.getValueSafe();
    }

    public String getPostedOn() {
        return postedOnProperty.getValueSafe();
    }


    @Override
    public Comment toEntity() {
        throw new UnsupportedOperationException("CommentModel#toEntity() not supported!");
    }
}
