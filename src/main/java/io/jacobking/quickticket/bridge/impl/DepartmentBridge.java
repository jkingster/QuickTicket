package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.DepartmentModel;
import io.jacobking.quickticket.tables.pojos.Department;

public class DepartmentBridge extends Bridge<Department, DepartmentModel> {
    public DepartmentBridge(final Database database) {
        super(database, RepoType.DEPARTMENT);
    }

    @Override public DepartmentModel convertEntity(Department entity) {
        return new DepartmentModel(entity);
    }
}
