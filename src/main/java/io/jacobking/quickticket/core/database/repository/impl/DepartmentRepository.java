package io.jacobking.quickticket.core.database.repository.impl;

import io.jacobking.quickticket.core.database.repository.Repository;
import io.jacobking.quickticket.tables.pojos.Department;
import io.jacobking.quickticket.tables.records.DepartmentRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

import static io.jacobking.quickticket.Tables.DEPARTMENT;

public class DepartmentRepository implements Repository<Department> {

    @Override public Department getById(DSLContext context, int id) {
        return context.selectFrom(DEPARTMENT)
                .where(DEPARTMENT.ID.eq(id))
                .fetchOneInto(Department.class);
    }

    @Override public Department save(DSLContext context, Department department) {
        return context.insertInto(DEPARTMENT)
                .set(new DepartmentRecord(department))
                .onConflictDoNothing()
                .returning()
                .fetchOneInto(Department.class);
    }

    @Override public List<Department> getAll(DSLContext context) {
        return context.selectFrom(DEPARTMENT)
                .fetchInto(Department.class);
    }

    @Override public List<Department> getAll(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean delete(DSLContext context, int id) {
        return context.deleteFrom(DEPARTMENT)
                .where(DEPARTMENT.ID.eq(id))
                .execute() == SUCCESS;
    }

    @Override public boolean update(DSLContext context, Department department) {
        final DepartmentRecord record = new DepartmentRecord(department);
        record.changed(DEPARTMENT.ID, false);
        return context.update(DEPARTMENT)
                .set(record)
                .where(DEPARTMENT.ID.eq(department.getId()))
                .execute() >= SUCCESS;
    }
}
