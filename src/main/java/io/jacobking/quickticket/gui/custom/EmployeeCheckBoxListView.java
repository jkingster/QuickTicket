package io.jacobking.quickticket.gui.custom;

import io.jacobking.quickticket.bridge.BridgeContext;
import io.jacobking.quickticket.gui.model.CompanyModel;
import io.jacobking.quickticket.gui.model.EmployeeModel;
import javafx.scene.control.cell.CheckBoxListCell;
import org.controlsfx.control.CheckListView;

public class EmployeeCheckBoxListView extends CheckListView<EmployeeModel> {
    public EmployeeCheckBoxListView(BridgeContext bridgeContext) {
        super(bridgeContext.getEmployee().getObservableList());

        setCellFactory(data -> new CheckBoxListCell<>(this::getItemBooleanProperty) {
            @Override public void updateItem(EmployeeModel employeeModel, boolean isEmpty) {
                super.updateItem(employeeModel, isEmpty);
                if (employeeModel == null || isEmpty) {
                    setText("");
                    return;
                }

                final int companyId = employeeModel.getCompanyIdProperty();
                final CompanyModel company = bridgeContext.getCompany().getModel(companyId);
                final String companyName = (company == null) ? "Unknown" : company.getName();

                final String employeeName = employeeModel.getFullName();
                setText(String.format("%s (Company: %s)", employeeName, companyName));
            }
        });
    }
}
