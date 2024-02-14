package io.jacobking.quickticket.core.database.repository;

import io.jacobking.quickticket.core.database.repository.impl.TicketRepository;

public enum RepoType {

    TICKET(new TicketRepository());

    private final Repository<? extends Entity> repository;

    RepoType(Repository<? extends Entity> repository) {
        this.repository = repository;
    }

    public Repository<? extends Entity> getRepository() {
        return repository;
    }
}
