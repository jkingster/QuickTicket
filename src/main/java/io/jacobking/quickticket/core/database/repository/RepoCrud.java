package io.jacobking.quickticket.core.database.repository;

import io.jacobking.quickticket.core.database.repository.impl.*;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class RepoCrud {
    private final Map<RepoType, Repository<? extends Entity>> repositoryMap = new HashMap<>();
    private final DSLContext                                  context;

    public RepoCrud(final DSLContext context) {
        this.context = context;
        loadRepositories();
    }

    public DSLContext getContext() {
        return context;
    }

    public <T extends Entity> T getById(final RepoType type, final int id) {
        return (T) getRepository(type).getById(context, id);
    }

    public <T extends Entity> T save(final RepoType type, final T value) {
        return (T) getRepository(type).save(context, value);
    }

    public boolean delete(final RepoType type, final int id) {
        return getRepository(type).delete(context, id);
    }

    public <T extends Entity> boolean update(final RepoType type, final T value) {
        return getRepository(type).update(context, value);
    }

    public <T extends Entity> List<T> getAll(final RepoType type) {
        return (List<T>) getRepository(type).getAll(context);
    }

    public <T extends Entity> List<T> getAll(final RepoType type, final Condition condition) {
        return (List<T>) getRepository(type).getAll(context, condition);
    }

    private <T extends Entity> Repository<T> getRepository(final RepoType repoType) {
        return (Repository<T>) repositoryMap.getOrDefault(repoType, null);
    }

    private <T extends Entity> void loadRepositories() {
        for (RepoType value : RepoType.values()) {
            repositoryMap.computeIfAbsent(value, type -> switch (type) {
                case TICKET -> (Repository<? extends Entity>) new TicketRepository();
                case COMMENT -> (Repository<? extends Entity>) new CommentRepository();
                case EMPLOYEE -> (Repository<? extends Entity>) new EmployeeRepository();
                case EMAIL -> (Repository<? extends Entity>) new EmailRepository();
                case ALERT -> (Repository<? extends Entity>) new AlertSettingsRepository();
                case PROFILE -> (Repository<? extends Entity>) new ProfilePictureRepository();
            });
        }
    }
}
