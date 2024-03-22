package io.jacobking.quickticket.core.database.repository.impl;

import io.jacobking.quickticket.core.database.repository.Repository;
import io.jacobking.quickticket.tables.pojos.Employee;
import io.jacobking.quickticket.tables.records.EmployeeRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

import static io.jacobking.quickticket.tables.Employee.EMPLOYEE;

public class EmployeeRepository implements Repository<Employee> {
    @Override
    public Employee getById(DSLContext context, int id) {
        return context.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.ID.eq(id))
                .fetchOneInto(Employee.class);
    }

    @Override
    public Employee save(DSLContext context, Employee employee) {
        return context.insertInto(EMPLOYEE)
                .set(new EmployeeRecord(employee))
                .onConflictDoNothing()
                .returning()
                .fetchOneInto(Employee.class);
    }

    @Override
    public List<Employee> getAll(DSLContext context) {
        return context.selectFrom(EMPLOYEE)
                .fetchInto(Employee.class);
    }

    @Override
    public List<Employee> getAll(DSLContext context, Condition condition) {
        return null;
    }

    @Override
    public boolean delete(DSLContext context, int id) {
        return context.deleteFrom(EMPLOYEE)
                .where(EMPLOYEE.ID.eq(id))
                .execute() == SUCCESS;
    }

    @Override
    public boolean update(DSLContext context, Employee employee) {
        final EmployeeRecord record = new EmployeeRecord(employee);
        record.changed(EMPLOYEE.ID, false);
        return context.update(EMPLOYEE)
                .set(record)
                .where(EMPLOYEE.ID.eq(employee.getId()))
                .execute() >= SUCCESS;
    }
}
