package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.impl.CompanyModel;
import io.jacobking.quickticket.tables.pojos.Company;

public class CompanyBridge extends Bridge<Company, CompanyModel> {
    public CompanyBridge(final Database database) {
        super(database, RepoType.COMPANY);
    }

    @Override public CompanyModel convertEntity(Company entity) {
        return new CompanyModel(entity);
    }
}
