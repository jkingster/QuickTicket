package io.jacobking.quickticket.core.database.repository;

import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

public interface Repository<T extends Entity> {
    int SUCCESS = 1;

    T getById(final DSLContext context, final int id);

    T save(final DSLContext context, final T t);

    List<T> getAll(final DSLContext context);

    List<T> getAll(DSLContext context, final Condition condition);

    boolean delete(final DSLContext context, final int id);

    boolean update(final DSLContext context, final T t);
}
