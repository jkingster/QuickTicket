package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.core.utility.MiscUtil;
import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.Data;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.model.CompanyModel;
import io.jacobking.quickticket.gui.model.DepartmentModel;
import io.jacobking.quickticket.gui.model.EmployeeModel;
import io.jacobking.quickticket.gui.model.TicketModel;
import io.jacobking.quickticket.gui.utility.FXUtility;
import io.jacobking.quickticket.gui.utility.IconLoader;
import io.jacobking.quickticket.tables.pojos.Employee;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeController extends Controller {

    private static final String MAIL_TO_LINK = "mailto:%s";

    @FXML private AnchorPane parent;
    @FXML private AnchorPane informationParent;

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
    @FXML private Label totalTicketCountLabel;

    @FXML private SearchableComboBox<CompanyModel>    employeeCompany;
    @FXML private SearchableComboBox<DepartmentModel> employeeDepartment;

    @FXML private CheckBox disableEmployeeCheckBox;
    @FXML private CheckBox accidentDeletionCheckBox;

    @FXML private TableView<TicketModel>                 ticketTable;
    @FXML private TableColumn<TicketModel, Void>         actionColumn;
    @FXML private TableColumn<TicketModel, String>       titleColumn;
    @FXML private TableColumn<TicketModel, StatusType>   statusColumn;
    @FXML private TableColumn<TicketModel, PriorityType> priorityColumn;
    @FXML private TableColumn<TicketModel, String>       dateColumn;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureButtons();
        configureCompanyFilter();
        configureDepartmentFilter();
        configureEmployeeSelector();
        configureLabels();
        configureEmployeeCompanyBox();
        configureEmployeeDepartmentBox();
        configureDisabledCheckBox();
        configureTicketTable();

        companyFilter.getSelectionModel().select(0);
        departmentFilter.getSelectionModel().select(0);
    }

    private void configureButtons() {
        createButton.setGraphic(IconLoader.getMaterialIcon(Material2MZ.OPEN_IN_NEW));
        deleteButton.setGraphic(IconLoader.getMaterialIcon(Material2AL.DELETE_FOREVER));
        updateButton.setGraphic(IconLoader.getMaterialIcon(Material2MZ.UPDATE));
        resetButton.setGraphic(IconLoader.getMaterialIcon(Material2AL.CLEAR_ALL));
        emailButton.setGraphic(IconLoader.getMaterialIcon(Material2MZ.SEND));

        final ReadOnlyObjectProperty<EmployeeModel> employeeSelectorProperty = employeeSelector.getSelectionModel().selectedItemProperty();
        createButton.disableProperty().bind(employeeSelectorProperty.isNotNull());
        deleteButton.disableProperty().bind(createButton.disabledProperty().not());
        updateButton.disableProperty().bind(createButton.disabledProperty().not());

        createButton.setOnAction(event -> onCreateEmployee());
        deleteButton.setOnAction(event -> onDeleteEmployee());
        updateButton.setOnAction(event -> onUpdateEmployee());
        resetButton.setOnAction(event -> {
            ticketTable.setItems(null);
            totalTicketCountLabel.setText("0");
            FXUtility.resetFields(informationParent);
            employeeSelector.getSelectionModel().clearSelection();
        });
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
                departmentFilter.setItems(FXCollections.observableArrayList(
                        bridgeContext.getDepartment().getObservableListByFilter(dm -> dm.getId() == 0)
                ));
                departmentFilter.getSelectionModel().selectFirst();
                departmentFilter.setDisable(true);
                return;
            }

            departmentFilter.setItems(bridgeContext.getDepartment().getObservableListByFilter(model -> {
                departmentFilter.setDisable(false);
                return model.getCompanyId() == companyId;
            }));
        });
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

            employeeSelector.setItems(bridgeContext.getEmployee().getObservableListByFilter(employee -> {
                return (employee.getCompanyIdProperty() == currentCompanyId) && (employee.getDepartmentIdProperty() == departmentId);
            }));
        });
    }

    private void configureEmployeeSelector() {
        employeeSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldEmployee, newEmployee) -> {
            if (newEmployee == null) {
                return;
            }
            populateFields(newEmployee);
        });

        employeeSelector.setConverter(new StringConverter<>() {
            @Override public String toString(EmployeeModel employeeModel) {
                return (employeeModel == null) ? "" : employeeModel.getFullName();
            }

            @Override public EmployeeModel fromString(String s) {
                return null;
            }
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

        final CompanyModel foundCompany = bridgeContext.getCompany().getModel(employeeModel.getCompanyIdProperty());
        if (foundCompany != null) {
            employeeCompany.getSelectionModel().select(foundCompany);
        }

        final DepartmentModel foundDepartment = bridgeContext.getDepartment().getModel(employeeModel.getDepartmentIdProperty());
        if (foundDepartment != null) {
            employeeDepartment.getSelectionModel().select(foundDepartment);
        }

        accidentDeletionCheckBox.setSelected(employeeModel.isPreventAccidentalDeletion());
        disableEmployeeCheckBox.setSelected(employeeModel.isIsDisabled());

        final var tickets = bridgeContext.getTicketEmployee()
                .getTicketsForEmployee(employeeModel.getId());

        totalTicketCountLabel.setText(tickets.size() + "");
        ticketTable.setItems(tickets);
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
        employeeSelector.getSelectionModel().select(newEmployee);
        selectFilters(newEmployee);
    }

    private void selectFilters(final EmployeeModel employee) {
        final int companyId = employee.getCompanyIdProperty();
        final CompanyModel companyModel = bridgeContext.getCompany().getModel(companyId);
        if (companyModel != null) {
            companyFilter.getSelectionModel().select(companyModel);
        }

        final int departmentId = employee.getDepartmentIdProperty();
        final DepartmentModel departmentModel = bridgeContext.getDepartment().getModel(departmentId);
        if (departmentModel != null) {
            departmentFilter.getSelectionModel().select(departmentModel);
        }
    }

    private void onDeleteEmployee() {
        if (getSelectedEmployee() == null) {
            Announcements.get().showError("Error", "Could not delete employee.", "No employee selected.");
            return;
        }

        final boolean isProtected = getSelectedEmployee().isPreventAccidentalDeletion();
        if (isProtected) {
            Announcements.get().showError("Error", "Action Prohibited", "This employee is protected from deletion.");
            return;
        }

        Announcements.get().showConfirmation(this::processEmployeeDeletion, "Confirmation", "Please confirm you want to delete this employee.").ifPresent(type -> {
            if (type == ButtonType.YES) {
                processEmployeeDeletion();
            }
        });
    }

    private void processEmployeeDeletion() {
        final boolean removed = bridgeContext.getEmployee().remove(getSelectedEmployee().getId());
        if (!removed) {
            Announcements.get().showError("Error", "Could not delete employee.", "No employee selected.");
            return;
        }

        FXUtility.resetFields(informationParent);
        totalTicketCountLabel.setText("0");
        ticketTable.setItems(null);
        Announcements.get().showConfirm("Success", "Employee deleted.");
    }

    private void onUpdateEmployee() {
        final EmployeeModel updated = getUpdatedEmployee();
        if (updated == null) {
            Announcements.get().showError("Error", "Could not update employee.", "Try again.");
            return;
        }

        if (!bridgeContext.getEmployee().update(updated)) {
            Announcements.get().showError("Error", "Could not update employee.", "Try again.");
            return;
        }

        Announcements.get().showConfirm("Success", "Employee successfully updated.");
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

    private void configureEmployeeCompanyBox() {
        employeeCompany.setItems(bridgeContext.getCompany().getObservableList());
        employeeCompany.setConverter(new StringConverter<>() {
            @Override public String toString(CompanyModel companyModel) {
                return (companyModel == null) ? "Unknown" : companyModel.getName();
            }

            @Override public CompanyModel fromString(String s) {
                return null;
            }
        });

        employeeCompany.getSelectionModel().selectedItemProperty().addListener((observable, oldCompany, newCompany) -> {
            if (newCompany == null) {
                return;
            }

            final int companyId = newCompany.getId();
            if (companyId == 0) {
                employeeDepartment.setItems(bridgeContext.getDepartment().getObservableList());
                return;
            }
            employeeDepartment.setItems(bridgeContext.getDepartment().getObservableListByFilter(__ -> __.getCompanyId() == companyId));
        });
    }

    private void configureEmployeeDepartmentBox() {
        employeeDepartment.setConverter(new StringConverter<>() {
            @Override public String toString(DepartmentModel departmentModel) {
                return (departmentModel == null) ? "Unknown" : departmentModel.getName();
            }

            @Override public DepartmentModel fromString(String s) {
                return null;
            }
        });
    }

    private void configureDisabledCheckBox() {
        disableEmployeeCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            firstNameField.setDisable(newValue);
            lastNameField.setDisable(newValue);
            emailField.setDisable(newValue);
            titleField.setDisable(newValue);
            commentsArea.setDisable(newValue);
            employeeCompany.setDisable(newValue);
            employeeDepartment.setDisable(newValue);
            cellPhoneField.setDisable(newValue);
            workPhoneField.setDisable(newValue);
            workPhoneExtensionField.setDisable(newValue);

            if (getSelectedEmployee() != null) {
                getSelectedEmployee().setIsDisabled(newValue);
            }
        });
    }

    private void configureTicketTable() {
        configureActionColumn();
        configureTitleColumn();
        configureStatusColumn();
        configurePriorityColumn();
        configureDateColumn();
    }

    private void configureActionColumn() {
        actionColumn.setCellFactory(data -> new TableCell<>() {

            private final HBox box = new HBox(2.5);

            {
                final Button open = new Button();
                open.setGraphic(IconLoader.getMaterialIcon(MaterialDesign.MDI_TICKET_CONFIRMATION));
                open.setOnAction(event -> onOpenTicket(getTableRow().getItem()));
                open.setTooltip(new Tooltip("Open Ticket"));

                final Button unlink = new Button();
                unlink.setGraphic(IconLoader.createDefault(FontAwesome.Glyph.UNLINK));
                unlink.setOnAction(event -> onUnlinkTicket(getTableRow().getItem()));
                unlink.setTooltip(new Tooltip("Unlink Ticket from Employee"));

                box.setAlignment(Pos.CENTER);
                box.getChildren().addAll(open, unlink);
            }

            private void onOpenTicket(final TicketModel ticketModel) {
                if (ticketModel == null) {
                    return;
                }
                display.show(Route.VIEWER, Data.of(ticketModel, ticketTable));
            }

            private void onUnlinkTicket(final TicketModel ticketModel) {
                final EmployeeModel employee = getSelectedEmployee();
                if (employee == null) {
                    return;
                }

                // TODO: Look at?
                final boolean removed = bridgeContext.getTicketEmployee().removeByEmployeeId(employee.getId());
                if (!bridgeContext.getTicket().update(ticketModel)) {
                    Announcements.get().showError("Error", "Failed to unlink employee.", "Please try again.");
                    return;
                }

                Announcements.get().showConfirm("Success", "Employee unlinked from ticket.");
                ticketTable.setItems(bridgeContext.getTicketEmployee().getTicketsForEmployee(employee.getId()));
            }

            @Override protected void updateItem(Void object, boolean empty) {
                super.updateItem(object, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }

                setGraphic(box);
            }
        });
    }

    private void configureTitleColumn() {
        titleColumn.setCellValueFactory(data -> data.getValue().titleProperty());
    }

    private void configureStatusColumn() {
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());
        statusColumn.setCellFactory(data -> new TableCell<>() {
            @Override protected void updateItem(StatusType statusType, boolean empty) {
                super.updateItem(statusType, empty);
                if (statusType == null || empty) {
                    setText(null);
                    return;
                }

                switch (statusType) {
                    case OPEN -> setStyle("-fx-text-fill: #3498DB;");
                    case RESOLVED -> setStyle("-fx-text-fill: #5DADD5;");
                    case ACTIVE -> setStyle("-fx-text-fill: #FF5733;");
                    case PAUSED -> setStyle("-fx-text-fill: #FFC300;");
                    default -> setStyle("-fx-text-fill: white");
                }

                setText(statusType.name());
            }
        });
    }

    private void configurePriorityColumn() {
        priorityColumn.setCellValueFactory(data -> data.getValue().priorityProperty());
        priorityColumn.setCellFactory(data -> new TableCell<>() {
            @Override protected void updateItem(PriorityType priorityType, boolean empty) {
                super.updateItem(priorityType, empty);
                if (priorityType == null || empty) {
                    setText(null);
                    return;
                }

                switch (priorityType) {
                    case LOW -> setStyle("-fx-text-fill: GREEN;");
                    case MEDIUM -> setStyle("-fx-text-fill: YELLOW;");
                    case HIGH -> setStyle("-fx-text-fill: ORANGE;");
                    default -> setStyle("-fx-text-fill: white;");
                }

                setText(priorityType.name());
            }
        });
    }

    private void configureDateColumn() {
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(
                DateUtil.formatDateTime(
                        DateUtil.DateFormat.DATE_TIME_ONE,
                        data.getValue().getCreation()
                )
        ));
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
        final DepartmentModel selected = employeeDepartment.getSelectionModel().getSelectedItem();
        return (selected == null) ? 0 : selected.getId();
    }

    private int getCompanyId() {
        final CompanyModel selected = employeeCompany.getSelectionModel().getSelectedItem();
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
                .setComments(commentsArea.getText())
                .setIsDisabled(isDisabled())
                .setPreventAccidentalDeletion(preventAccidentalDeletion());
    }

    private boolean preventAccidentalDeletion() {
        return accidentDeletionCheckBox.isSelected();
    }

    private boolean isDisabled() {
        return disableEmployeeCheckBox.isSelected();
    }

    private EmployeeModel getUpdatedEmployee() {
        final EmployeeModel selected = getSelectedEmployee();
        if (selected != null) {
            selected.setFirstName(firstNameField.getText());
            selected.setLastName(lastNameField.getText());
            selected.setTitle(getTitle());
            selected.setEmail(getEmail());
            selected.setDepartmentIdProperty(getDepartmentId());
            selected.setCompanyIdProperty(getCompanyId());
            selected.setWorkPhoneProperty(getWorkPhone());
            selected.setWorkExtensionProperty(0);
            selected.setMobilePhoneProperty(getCellPhone());
            selected.setMiscInfoProperty(commentsArea.getText());
            selected.setPreventAccidentalDeletion(preventAccidentalDeletion());
            selected.setIsDisabled(isDisabled());
            return selected;
        }
        return null;
    }

}
