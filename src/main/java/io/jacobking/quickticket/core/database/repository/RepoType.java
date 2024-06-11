package io.jacobking.quickticket.core.database.repository;


import io.jacobking.quickticket.core.database.repository.impl.*;

public enum RepoType {

    TICKET(new TicketRepository()),
    COMMENT(new CommentRepository()),
    EMPLOYEE(new EmployeeRepository()),
    EMAIL(new EmailRepository()),
    ALERT(new AlertSettingsRepository()),
    COMPANY(new CompanyRepository()),
    DEPARTMENT(new DepartmentRepository()),
    FLYWAY(new FlywayRepository()),
    LINKED_TICKET(new LinkedTicketRepository());

    private final Repository<? extends Entity> repository;

    RepoType(Repository<? extends Entity> repository) {
        this.repository = repository;
    }

    public Repository<? extends Entity> getRepository() {
        return repository;
    }
}
