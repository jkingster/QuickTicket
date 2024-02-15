package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.impl.EmployeeModel;
import io.jacobking.quickticket.tables.pojos.Employee;

public class EmployeeBridge extends Bridge<Employee, EmployeeModel> {
    public EmployeeBridge() {
        super(RepoType.EMPLOYEE);
    }

    @Override
    public EmployeeModel convertEntity(Employee entity) {
        return new EmployeeModel(entity);
    }
}
