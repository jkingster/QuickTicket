package io.jacobking.quickticket.core.database.repository.impl;

import io.jacobking.quickticket.core.database.repository.Repository;
import io.jacobking.quickticket.tables.pojos.Comment;
import io.jacobking.quickticket.tables.records.CommentRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

import static io.jacobking.quickticket.Tables.COMMENT;

public class CommentRepository implements Repository<Comment> {
    @Override
    public Comment getById(DSLContext context, int id) {
        throw new UnsupportedOperationException("CommentRepository#getById(DSLContext, Integer) not supported!");
    }

    @Override
    public Comment save(DSLContext context, Comment comment) {
        return context.insertInto(COMMENT)
                .set(new CommentRecord(comment))
                .onConflictDoNothing()
                .returning()
                .fetchOneInto(Comment.class);
    }

    @Override
    public List<Comment> getAll(DSLContext context) {
        return context.selectFrom(COMMENT)
                .fetchInto(Comment.class);
    }

    @Override
    public List<Comment> getAll(DSLContext context, Condition condition) {
        return context.selectFrom(COMMENT)
                .where(condition)
                .fetchInto(Comment.class);
    }

    @Override
    public boolean delete(DSLContext context, int id) {
        return context.deleteFrom(COMMENT)
                .where(COMMENT.ID.eq(id))
                .execute() == SUCCESS;
    }

    @Override public boolean deleteWhere(DSLContext context, Condition condition) {
        return context.deleteFrom(COMMENT)
                .where(condition)
                .execute() == SUCCESS;
    }

    @Override
    public boolean update(DSLContext context, Comment comment) {
        throw new UnsupportedOperationException("CommentRepository#update(DSLContext, Comment) not supported!");

    }
}
