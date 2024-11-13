package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.core.utility.MiscUtil;
import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.model.CompanyModel;
import io.jacobking.quickticket.gui.model.DepartmentModel;
import io.jacobking.quickticket.gui.model.EmployeeModel;
import io.jacobking.quickticket.gui.utility.FXUtility;
import io.jacobking.quickticket.gui.utility.IconLoader;
import io.jacobking.quickticket.tables.pojos.Employee;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeController extends Controller {

    private static final String MAIL_TO_LINK = "mailto:%s";

    @FXML private AnchorPane parent;

    @FXML private Button createButton;
    @FXML private Button deleteButton;
    @FXML private Button updateButton;
    @FXML private Button resetButton;

    @FXML private SearchableComboBox<CompanyModel>    companyFilter;
    @FXML private SearchableComboBox<DepartmentModel> departmentFilter;
    @FXML private SearchableComboBox<EmployeeModel>   employeeSelector;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private Button    emailButton;
    @FXML private TextField workPhoneField;
    @FXML private TextField workPhoneExtensionField;
    @FXML private TextField cellPhoneField;
    @FXML private TextField titleField;
    @FXML private TextArea  commentsArea;

    @FXML private Label personLabel;
    @FXML private Label emailLabel;
    @FXML private Label workPhoneLabel;
    @FXML private Label cellPhoneLabel;
    @FXML private Label titleLabel;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureButtons();
        configureCompanyFilter();
        configureDepartmentFilter();
        configureEmployeeSelector();
        configureLabels();
    }

    private void configureButtons() {
        createButton.setGraphic(IconLoader.getMaterialIcon(Material2MZ.OPEN_IN_NEW));
        deleteButton.setGraphic(IconLoader.getMaterialIcon(Material2AL.DELETE_FOREVER));
        updateButton.setGraphic(IconLoader.getMaterialIcon(Material2MZ.UPDATE));
        resetButton.setGraphic(IconLoader.getMaterialIcon(Material2AL.CLEAR_ALL));
        emailButton.setGraphic(IconLoader.getMaterialIcon(Material2MZ.SEND));

        final ReadOnlyObjectProperty<EmployeeModel> employeeSelectorProperty
                = employeeSelector.getSelectionModel().selectedItemProperty();
        createButton.disableProperty().bind(employeeSelectorProperty.isNotNull());
        deleteButton.disableProperty().bind(createButton.disabledProperty().not());
        updateButton.disableProperty().bind(createButton.disabledProperty().not());

        createButton.setOnAction(event -> onCreateEmployee());
        deleteButton.setOnAction(event -> onDeleteEmployee());
        updateButton.setOnAction(event -> onUpdateEmployee());
        resetButton.setOnAction(event -> FXUtility.resetFields(parent));
        emailButton.setOnAction(event -> onOpenEmail());
    }

    private void configureCompanyFilter() {
        companyFilter.setItems(bridgeContext.getCompany().getObservableList());
        companyFilter.setConverter(new StringConverter<>() {
            @Override public String toString(CompanyModel companyModel) {
                return (companyModel == null) ? "Unknown" : companyModel.getName();
            }

            @Override public CompanyModel fromString(String s) {
                return null;
            }
        });

        companyFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldCompany, newCompany) -> {
            if (newCompany == null) {
                return;
            }

            final int companyId = newCompany.getId();
            if (companyId == 0) {
                departmentFilter.setItems(FXCollections.observableArrayList(bridgeContext.getDepartment().getFirst()));
                departmentFilter.getSelectionModel().selectFirst();
                return;
            }

            departmentFilter.setItems(bridgeContext.getDepartment().getObservableListByFilter(model -> {
                return model.getCompanyId() == companyId;
            }));
        });

        companyFilter.getSelectionModel().select(0);
    }

    private void configureDepartmentFilter() {
        departmentFilter.setConverter(new StringConverter<>() {
            @Override public String toString(DepartmentModel departmentModel) {
                return (departmentModel == null) ? "Unknown" : departmentModel.getName();
            }

            @Override public DepartmentModel fromString(String s) {
                return null;
            }
        });

        departmentFilter.getSelectionModel().selectedItemProperty().addListener((observable, oldDepartment, newDepartment) -> {
            if (newDepartment == null) {
                return;
            }

            final CompanyModel currentCompany = companyFilter.getSelectionModel().getSelectedItem();
            final int currentCompanyId = (currentCompany == null) ? 0 : currentCompany.getId();

            final int departmentId = newDepartment.getId();
            if (currentCompanyId == 0 && departmentId == 0) {
                employeeSelector.setItems(bridgeContext.getEmployee().getObservableList());
                return;
            }

            if (departmentId == 0) {
                employeeSelector.setItems(bridgeContext.getEmployee().getObservableListByFilter(employee -> {
                    return (employee.getCompanyIdProperty() == currentCompanyId);
                }));
                return;
            }

            employeeSelector.setItems(bridgeContext.getEmployee().getObservableListByFilter(employee -> {
                return (employee.getCompanyIdProperty() == currentCompanyId)
                        && (employee.getDepartmentIdProperty() == departmentId);
            }));
        });

        departmentFilter.getSelectionModel().select(0);
    }

    private void configureEmployeeSelector() {
        employeeSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldEmployee, newEmployee) -> {
            if (newEmployee == null) {
                return;
            }
            populateFields(newEmployee);
        });
    }

    private void populateFields(final EmployeeModel employeeModel) {
        firstNameField.setText(employeeModel.getFirstName());
        lastNameField.setText(employeeModel.getLastName());
        emailField.setText(employeeModel.getEmail());
        cellPhoneField.setText(employeeModel.getMobilePhoneProperty());
        workPhoneField.setText(employeeModel.getWorkPhoneProperty());
        workPhoneExtensionField.setText(String.valueOf(employeeModel.getWorkExtensionProperty()));
        titleField.setText(employeeModel.getTitle());
        commentsArea.setText(employeeModel.getMiscInfoProperty());
    }

    private void onCreateEmployee() {
        if (getSelectedEmployee() != null) {
            return;
        }

        final String firstName = firstNameField.getText();
        final String lastName = lastNameField.getText();
        if (firstName.isEmpty() || lastName.isEmpty()) {
            Announcements.get().showError("Error", "Could not create employee.", "First and last name required.");
            return;
        }

        final EmployeeModel newEmployee = bridgeContext.getEmployee().createModel(getNewEmployee(firstName, lastName));
        if (newEmployee == null) {
            Announcements.get().showError("Error", "Could not create employee.", "Please try again.");
            return;
        }

        Announcements.get().showConfirm("Success", "Employee created successfully.");
    }

    private void onDeleteEmployee() {

    }

    private void onUpdateEmployee() {

    }

    private void onOpenEmail() {
        final String email = getEmail();
        if (email == null || email.equalsIgnoreCase("N/A")) {
            Announcements.get().showError("Error", "Could not open e-mail.", "Employee has no attached e-mail.");
            return;
        }
        MiscUtil.openLink(MAIL_TO_LINK.formatted(email));
    }

    private void configureLabels() {
        personLabel.setGraphic(IconLoader.getMaterialIcon(Material2MZ.PERSON));
        emailLabel.setGraphic(IconLoader.getMaterialIcon(Material2AL.EMAIL));
        workPhoneLabel.setGraphic(IconLoader.getMaterialIcon(Material2AL.LOCAL_PHONE));
        cellPhoneLabel.setGraphic(IconLoader.getMaterialIcon(Material2MZ.PHONE_IPHONE));
        titleLabel.setGraphic(IconLoader.getMaterialIcon(Material2MZ.PERM_IDENTITY));
    }

    // Utilities
    private EmployeeModel getSelectedEmployee() {
        return employeeSelector.getSelectionModel().getSelectedItem();
    }

    private String getTitle() {
        return titleField.getText().isEmpty() ? "N/A" : titleField.getText();
    }

    private String getEmail() {
        return emailField.getText().isEmpty() ? "N/A" : emailField.getText();
    }

    private int getDepartmentId() {
        final DepartmentModel selected = departmentFilter.getSelectionModel().getSelectedItem();
        return (selected == null) ? 0 : selected.getId();
    }

    private int getCompanyId() {
        final CompanyModel selected = companyFilter.getSelectionModel().getSelectedItem();
        return (selected == null) ? 0 : selected.getId();
    }

    private String getWorkPhone() {
        return workPhoneField.getText().isEmpty() ? "" : workPhoneField.getText();
    }

    private String getWorkPhoneExtension() {
        return workPhoneExtensionField.getText().isEmpty() ? "" : workPhoneExtensionField.getText();
    }

    private String getCellPhone() {
        return cellPhoneField.getText().isEmpty() ? "" : cellPhoneField.getText();
    }

    private Employee getNewEmployee(final String firstName, final String lastName) {
        return new Employee()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setTitle(getTitle())
                .setEmail(getEmail())
                .setDepartmentId(getDepartmentId())
                .setCompanyId(getCompanyId())
                .setWorkPhone(getWorkPhone())
                .setWorkExtension(0)
                .setMobilePhone(getCellPhone())
                .setComments(commentsArea.getText());
    }

}
