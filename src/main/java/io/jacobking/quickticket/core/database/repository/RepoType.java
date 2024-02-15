package io.jacobking.quickticket.core.database.repository;


import io.jacobking.quickticket.core.database.repository.impl.CommentRepository;
import io.jacobking.quickticket.core.database.repository.impl.EmployeeRepository;
import io.jacobking.quickticket.core.database.repository.impl.TicketRepository;

public enum RepoType {

    TICKET(new TicketRepository()),
    COMMENT(new CommentRepository()),
    EMPLOYEE(new EmployeeRepository());

    private final Repository<? extends Entity> repository;

    RepoType(Repository<? extends Entity> repository) {
        this.repository = repository;
    }

    public Repository<? extends Entity> getRepository() {
        return repository;
    }
}
