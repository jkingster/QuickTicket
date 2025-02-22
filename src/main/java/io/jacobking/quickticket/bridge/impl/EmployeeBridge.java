package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.EmployeeModel;
import io.jacobking.quickticket.tables.pojos.Employee;
import javafx.collections.ObservableList;

import java.util.Comparator;

public class EmployeeBridge extends Bridge<Employee, EmployeeModel> {
    public EmployeeBridge(final Database database) {
        super(database, RepoType.EMPLOYEE);
    }

    @Override
    public EmployeeModel convertEntity(Employee entity) {
        return new EmployeeModel(entity);
    }

    @Override public ObservableList<EmployeeModel> getObservableList() {
        return super.getObservableList().sorted(Comparator.comparing(EmployeeModel::getFullName));
    }

    public ObservableList<EmployeeModel> getListByOrganization(final int companyId, final int departmentId) {
        return getObservableListByFilter(employeeModel -> employeeModel.getCompanyIdProperty() == companyId
                && employeeModel.getDepartmentIdProperty() == departmentId);
    }
}
