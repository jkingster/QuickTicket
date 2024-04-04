package io.jacobking.quickticket.core.database.repository.impl;

import io.jacobking.quickticket.core.database.repository.Repository;
import io.jacobking.quickticket.tables.pojos.Company;
import io.jacobking.quickticket.tables.records.CompanyRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

import static io.jacobking.quickticket.Tables.COMPANY;

public class CompanyRepository implements Repository<Company> {
    @Override public Company getById(DSLContext context, int id) {
        return context.selectFrom(COMPANY)
                .where(COMPANY.ID.eq(id))
                .fetchOneInto(Company.class);
    }

    @Override public Company save(DSLContext context, Company company) {
        return context.insertInto(COMPANY)
                .set(new CompanyRecord(company))
                .onConflictDoNothing()
                .returning()
                .fetchOneInto(Company.class);
    }

    @Override public List<Company> getAll(DSLContext context) {
        return context.selectFrom(COMPANY)
                .fetchInto(Company.class);
    }

    @Override public List<Company> getAll(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean deleteWhere(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean delete(DSLContext context, int id) {
        return context.deleteFrom(COMPANY)
                .where(COMPANY.ID.eq(id))
                .execute() == SUCCESS;
    }

    @Override public boolean update(DSLContext context, Company company) {
        final CompanyRecord companyRecord = new CompanyRecord(company);
        companyRecord.changed(COMPANY.ID, false);
        return context.update(COMPANY)
                .set(companyRecord)
                .where(COMPANY.ID.eq(company.getId()))
                .execute() >= SUCCESS;
    }
}
