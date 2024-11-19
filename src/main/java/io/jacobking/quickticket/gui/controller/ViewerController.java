package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.Data;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.misc.PopOverBuilder;
import io.jacobking.quickticket.gui.model.EmployeeModel;
import io.jacobking.quickticket.gui.model.TicketCategoryModel;
import io.jacobking.quickticket.gui.model.TicketModel;
import io.jacobking.quickticket.gui.utility.IconLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.SearchableComboBox;
import org.kordamp.ikonli.material2.Material2MZ;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ViewerController extends Controller {

    private TicketModel            ticket;
    private TableView<TicketModel> ticketTable;

    @FXML private TextField                               ticketIdField;
    @FXML private TextField                               titleField;
    @FXML private SearchableComboBox<StatusType>          statusBox;
    @FXML private SearchableComboBox<PriorityType>        priorityBox;
    @FXML private TextField                               employeeField;
    @FXML private SearchableComboBox<TicketCategoryModel> categoryBox;
    @FXML private TextField                               createdField;
    @FXML private Button                                  findEmployeeButton;
    @FXML private Button                                  deleteEmployeeButton;


    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ticket = data.mapIndex(0, TicketModel.class);
        this.ticketTable = data.mapTable(1);

        if (ticket == null) {
            display.close(Route.VIEWER);
            return;
        }

        configureStatusBox();
        configurePriorityBox();
        populateFields();
        configureButtons();
    }

    private void configureStatusBox() {
        statusBox.setItems(FXCollections.observableArrayList(StatusType.values()));
        statusBox.setConverter(new StringConverter<StatusType>() {
            @Override public String toString(StatusType statusType) {
                return (statusType == null) ? "Unknown" : statusType.name();
            }

            @Override public StatusType fromString(String s) {
                return null;
            }
        });
    }

    private void configurePriorityBox() {
        priorityBox.setItems(FXCollections.observableArrayList(PriorityType.values()));
        priorityBox.setConverter(new StringConverter<PriorityType>() {
            @Override public String toString(PriorityType priorityType) {
                return (priorityType == null) ? "Unknown" : priorityType.name();
            }

            @Override public PriorityType fromString(String s) {
                return null;
            }
        });
    }


    private void populateFields() {
        ticketIdField.setText(ticket.getId() + "");
        titleField.setText(ticket.getTitle());
        statusBox.getSelectionModel().select(StatusType.of(ticket.getStatus()));
        priorityBox.getSelectionModel().select(PriorityType.of(ticket.getPriority()));

        final TicketCategoryModel category = bridgeContext.getCategory().getModel(ticket.getCategory());
        if (category != null) {
            categoryBox.getSelectionModel().select(category);
        }

        createdField.setText(DateUtil.formatDateTime(
                DateUtil.DateFormat.DATE_TIME_ONE,
                ticket.getCreation()
        ));

        findAttachedEmployees();
    }

    private void configureButtons() {
        findEmployeeButton.setGraphic(IconLoader.getMaterialIcon(Material2MZ.PERSON_ADD));
        deleteEmployeeButton.setGraphic(IconLoader.getMaterialIcon(Material2MZ.PERSON_REMOVE));
    }

    private void findAttachedEmployees() {
        final ObservableList<EmployeeModel> attachedEmployees = bridgeContext.getTicketEmployee()
                .getEmployeeModelsForTicket(ticket.getId());

        if (attachedEmployees.isEmpty())
            return;

        final String employees = attachedEmployees.stream()
                .map(EmployeeModel::getFullName)
                .collect(Collectors.joining(", "));

        employeeField.setText(employees);
    }

    @FXML private void onUpdateTicket() {
        final String title = titleField.getText();
        if (title == null || title.isEmpty()) {
            Announcements.get().showError("Error", "Could not update ticket.", "Ticket title required.");
            return;
        }


    }

    @FXML private void onFindEmployees() {
        display.show(Route.FIND_EMPLOYEE, Data.of(ticket, employeeField));
    }

    @FXML private void onDeleteEmployees() {
        PopOverBuilder.build()
                .setTitle("Remove Employees")
                .setDetached(true)
                .setAnimated(true)
                .process(this::getDeleteBox)
                .show(deleteEmployeeButton, 10);
    }

    private static final int NTH_MODIFIER = 50, BASE_HEIGHT = 15, INSET = 10;

    private Pane getDeleteBox(PopOverBuilder popOver) {
        final ObservableList<EmployeeModel> employeeList = bridgeContext.getTicketEmployee()
                .getEmployeeModelsForTicket(ticket.getId());

        final Pane pane = new Pane();
        pane.setPadding(new Insets(INSET, 0, INSET, 0));

        final VBox vBox = new VBox(5.0);
        vBox.setAlignment(Pos.CENTER);

        final double calculatedHeight = (NTH_MODIFIER * employeeList.size()) + BASE_HEIGHT;
        vBox.setPrefHeight(calculatedHeight);

        final CheckListView<EmployeeModel> checkListView = getCheckListView(employeeList);

        final Button delete = new Button("Delete");
        delete.setOnAction(event -> deleteEmployeeFromTicket(popOver, ticket.getId(), checkListView));

        vBox.getChildren().addAll(checkListView, delete);
        pane.getChildren().add(vBox);
        return pane;
    }

    private CheckListView<EmployeeModel> getCheckListView(final ObservableList<EmployeeModel> employeeModels) {
        return new CheckListView<>(employeeModels);
    }

    private void deleteEmployeeFromTicket(PopOverBuilder popOver, final int ticketId, final CheckListView<EmployeeModel> listView) {
        for (final EmployeeModel remove : listView.getCheckModel().getCheckedItems()) {
            final int employeeId = remove.getId();
            bridgeContext.getTicketEmployee().removeByTicketAndEmployeeId(ticketId, employeeId);
        }

        popOver.hide();
        resetEmployeeField();
    }

    private void resetEmployeeField() {
        final String employees = bridgeContext.getTicketEmployee().getEmployeeModelsForTicket(ticket.getId())
                .stream()
                .map(EmployeeModel::getFullName)
                .collect(Collectors.joining(", "));

        employeeField.setText(employees);
    }

    // Utilities


    private int getCategoryId() {
        final TicketCategoryModel category = categoryBox.getValue();
        return (category == null) ? 0 : category.getId();
    }

    private TicketModel getUpdatedTicket() {
        ticket.titleProperty().setValue(titleField.getText());
        ticket.statusProperty().setValue(statusBox.getValue());
        ticket.priorityProperty().setValue(priorityBox.getValue());
        ticket.categoryProperty().setValue(getCategoryId());
        return ticket;
    }
}
