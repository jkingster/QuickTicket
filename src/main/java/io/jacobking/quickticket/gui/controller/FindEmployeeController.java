package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.custom.CompanySearchBox;
import io.jacobking.quickticket.gui.custom.DepartmentSearchBox;
import io.jacobking.quickticket.gui.model.CompanyModel;
import io.jacobking.quickticket.gui.model.EmployeeModel;
import io.jacobking.quickticket.gui.model.TicketEmployeeModel;
import io.jacobking.quickticket.gui.model.TicketModel;
import io.jacobking.quickticket.gui.utility.FXUtility;
import io.jacobking.quickticket.tables.pojos.TicketEmployee;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FindEmployeeController extends Controller {

    private final ObservableList<EmployeeModel> addEmployeeList = FXCollections.observableArrayList();

    private TicketModel ticket;
    private TextField   employeeField;

    @FXML private Parent                       parent;
    @FXML private CheckBox                     allCheckBox;
    @FXML private CompanySearchBox             companyBox;
    @FXML private DepartmentSearchBox          departmentBox;
    @FXML private CheckComboBox<EmployeeModel> employeeBox;
    @FXML private Label                        countLabel;
    @FXML private Button                       acceptButton;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ticket = data.mapIndex(0, TicketModel.class);
        this.employeeField = data.mapIndex(1, TextField.class);

        configureCheckBox();
        configureCompanyBox();
        configureDepartmentBox();
        configureEmployeeBox();

        acceptButton.disableProperty().bind(Bindings.isEmpty(addEmployeeList));
    }

    private void configureCheckBox() {
        allCheckBox.selectedProperty().addListener((obs, oldStatus, newStatus) -> {
            if (newStatus == null) {
                return;
            }

            employeeBox.getItems().clear();
            companyBox.setDisable(newStatus);
            departmentBox.setDisable(newStatus);

            if (newStatus) {
                employeeBox.getItems().addAll(bridgeContext.getEmployee().getObservableList());
            }
        });
    }

    private void configureCompanyBox() {
        companyBox.setItems(bridgeContext.getCompany().getObservableListByFilter(companyModel ->
                companyModel.getId() != 0));

        companyBox.getSelectionModel().selectedItemProperty().addListener((obs, oldComp, newComp) -> {
            if (newComp == null) {
                return;
            }

            final int companyId = newComp.getId();
            final CompanyModel company = bridgeContext.getCompany().getModel(companyId);
            if (company != null) {
                departmentBox.setItems(bridgeContext.getDepartment().getObservableListByFilter(departmentModel ->
                        departmentModel.getCompanyId() == companyId));
            }
        });
    }

    private void configureDepartmentBox() {
        departmentBox.getSelectionModel().selectedItemProperty().addListener((obs, oldDep, newDep) -> {
            if (newDep == null) {
                return;
            }

            employeeBox.getItems().clear();
            final int departmentId = newDep.getId();
            employeeBox.getItems().addAll(bridgeContext.getEmployee().getObservableListByFilter(em ->
                    em.getDepartmentIdProperty() == departmentId));
        });
    }

    private void configureEmployeeBox() {
        employeeBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<EmployeeModel>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    final var subList = change.getAddedSubList();
                    addToList(subList);
                }

                if (change.wasRemoved()) {
                    final var subList = change.getRemoved();
                    addEmployeeList.removeAll(subList);
                }

                final int count = addEmployeeList.size();
                countLabel.setText(String.valueOf(count));
            }
        });
    }

    private void addToList(final List<? extends EmployeeModel> subList) {
        for (final EmployeeModel model : subList) {
            if (addEmployeeList.contains(model)) {
                continue;
            }
            addEmployeeList.add(model);
        }
    }

    @FXML private void onAccept() {
        final int ticketId = ticket.getId();
        final ObservableList<EmployeeModel> createdList = FXCollections.observableArrayList();

        int failedCounter = 0;
        for (final EmployeeModel employeeModel : addEmployeeList) {
            final TicketEmployeeModel attachedEmployee = createNewTicketEmployee(ticketId, employeeModel);
            if (attachedEmployee == null) {
                failedCounter++;
                continue;
            }
            createdList.add(employeeModel);
        }

        if (failedCounter > 0) {
            Announcements.get().showError("Error", "Could not add employee.", "One or more employees could not be attached to the ticket.");
        }

        resetEmployeeField(createdList);
        display.close(Route.FIND_EMPLOYEE);
    }

    private void resetEmployeeField(final ObservableList<EmployeeModel> employeeModels) {
        final String attachedEmployees = employeeModels.stream()
                .map(EmployeeModel::getFullName)
                .collect(Collectors.joining(", "));

        employeeField.setText(attachedEmployees.isEmpty() ? "Failed to load data." : attachedEmployees);
    }

    private TicketEmployeeModel createNewTicketEmployee(final int ticketId, final EmployeeModel model) {
        if (bridgeContext.getTicketEmployee().isEmployeeAssignedToTicket(ticketId, model.getId())) {
            return null;
        }

        return bridgeContext.getTicketEmployee().createModel(new TicketEmployee()
                .setTicketId(ticketId)
                .setEmployeeId(model.getId())
        );
    }

    @FXML private void onReset() {
        FXUtility.resetFields(parent);
        addEmployeeList.clear();
    }

    @FXML private void onCancel() {
        display.close(Route.FIND_EMPLOYEE);
    }
}

