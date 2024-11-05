package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.model.CommentModel;
import io.jacobking.quickticket.tables.pojos.Comment;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.jooq.impl.DSL;

import static io.jacobking.quickticket.Tables.COMMENT;

public class CommentBridge extends Bridge<Comment, CommentModel> {

    public CommentBridge(final Database database) {
        super(database, RepoType.COMMENT);
    }

    public ObservableList<CommentModel> getCommentsByTicketId(final int ticketId) {
        return getObservableList().filtered(cm -> cm.getTicketId() == ticketId);
    }

    @Override public CommentModel convertEntity(Comment entity) {
        return new CommentModel(entity);
    }

    public void removeCommentsByTicketId(final int ticketId) {
        Platform.runLater(() -> getObservableList().removeIf(cm -> cm.getTicketId() == ticketId));
        if (!crud.deleteWhere(RepoType.COMMENT, DSL.condition(COMMENT.TICKET_ID.eq(ticketId)))) {
            Announcements.get().showError(
                    "Failed to delete comments.",
                    "Could not delete all comments associated with deleted ticket."
            );
        }
    }

}
