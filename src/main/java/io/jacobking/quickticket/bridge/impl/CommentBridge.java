package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.impl.CommentModel;
import io.jacobking.quickticket.tables.pojos.Comment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

public class CommentBridge extends Bridge<Comment, CommentModel> {

    private final Map<Integer, ObservableList<CommentModel>> commentMap = new HashMap<>();

    public CommentBridge() {
        super(RepoType.COMMENT);

        getObservableList().forEach(model -> {
            final int ticketId = model.getTicketId();
            if (commentMap.containsKey(ticketId)) {
                pushComment(ticketId, model);
                return;
            }

            commentMap.put(ticketId, FXCollections.observableArrayList(model));
        });
    }

    @Override
    public CommentModel createModel(Comment entity) {
        final CommentModel model = super.createModel(entity);
        pushComment(model.getTicketId(), model);
        return model;
    }

    private void pushComment(final int ticketId, final CommentModel model) {
        ObservableList<CommentModel> commentModels = commentMap.get(ticketId);
        if (commentModels == null) {
            commentModels = FXCollections.observableArrayList();
            commentModels.add(model);
            commentMap.put(ticketId, commentModels);
            return;
        }

        commentModels.add(model);
    }

    public ObservableList<CommentModel> getComments(final int ticketId) {
        return commentMap.getOrDefault(ticketId, FXCollections.observableArrayList());
    }

    @Override
    public CommentModel convertEntity(Comment entity) {
        return new CommentModel(entity);
    }
}
