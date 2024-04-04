package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.impl.CommentModel;
import io.jacobking.quickticket.tables.pojos.Comment;
import javafx.collections.ObservableList;

import java.util.Comparator;

public class CommentBridge extends Bridge<Comment, CommentModel> {

    public CommentBridge() {
        super(RepoType.COMMENT);
    }

    public ObservableList<CommentModel> getCommentsByTicketId(final int ticketId) {
        return getObservableList().filtered(cm -> cm.getTicketId() == ticketId);
    }

    @Override public CommentModel convertEntity(Comment entity) {
        return new CommentModel(entity);
    }
}
