package io.jacobking.quickticket.core.database.entity.impl;

import io.jacobking.quickticket.core.database.entity.Entity;

public class CommentEntity extends Entity {

    private String comment;
    private String commentedOn;

    public CommentEntity(int id) {
        super(id);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentedOn() {
        return commentedOn;
    }

    public void setCommentedOn(String commentedOn) {
        this.commentedOn = commentedOn;
    }
}
