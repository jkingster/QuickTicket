package io.jacobking.quickticket.core.database.repository;

import io.jacobking.quickticket.core.database.repository.impl.*;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class RepoCrud {
    private final Map<RepoType, Repository<?>> repositoryMap = new HashMap<>();
    private final DSLContext                                  context;

    public RepoCrud(final DSLContext context) {
        this.context = context;
        loadRepositories();
    }

    public DSLContext getContext() {
        return context;
    }

    public <T> T getById(final RepoType type, final int id) {
        return (T) getRepository(type).getById(context, id);
    }

    public <T> T save(final RepoType type, final T value) {
        return (T) getRepository(type).save(context, value);
    }

    public boolean deleteWhere(final RepoType type, final int id) {
        return getRepository(type).delete(context, id);
    }

    public boolean deleteWhere(final RepoType type, final Condition condition) {
        return getRepository(type).deleteWhere(context, condition);
    }

    public <T> boolean update(final RepoType type, final T value) {
        return getRepository(type).update(context, value);
    }

    public <T> List<T> getAll(final RepoType type) {
        return (List<T>) getRepository(type).getAll(context);
    }

    public <T> List<T> getAll(final RepoType type, final Condition condition) {
        return (List<T>) getRepository(type).getAll(context, condition);
    }

    private <T> Repository<T> getRepository(final RepoType repoType) {
        return (Repository<T>) repositoryMap.getOrDefault(repoType, null);
    }

    private void loadRepositories() {
        for (RepoType value : RepoType.values()) {
            repositoryMap.computeIfAbsent(value, type -> switch (type) {
                case TICKET -> (Repository<?>) new TicketRepository();
                case ALERT -> (Repository<?>) new AlertRepository();
                case COMMENT -> (Repository<?>) new CommentRepository();
                case EMPLOYEE -> (Repository<?>) new EmployeeRepository();
                case COMPANY -> (Repository<?>) new CompanyRepository();
                case DEPARTMENT -> (Repository<?>) new DepartmentRepository();
                case CATEGORY -> (Repository<?>) new CategoryRepository();
                case TICKET_LINK -> (Repository<?>) new LinkRepository();
                case MODULE -> (Repository<?>) new ModuleRepository();
                case TICKET_EMPLOYEES -> (Repository<?>) new TicketEmployeeRepository();
            });
        }
    }
}
