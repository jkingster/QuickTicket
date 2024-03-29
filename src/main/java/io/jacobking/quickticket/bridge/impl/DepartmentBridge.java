package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.impl.DepartmentModel;
import io.jacobking.quickticket.tables.pojos.Department;

public class DepartmentBridge extends Bridge<Department, DepartmentModel> {
    public DepartmentBridge() {
        super(RepoType.DEPARTMENT);
    }

    @Override public DepartmentModel convertEntity(Department entity) {
        return new DepartmentModel(entity);
    }
}
