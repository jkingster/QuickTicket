package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.bridge.impl.TicketBridge;
import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.core.utility.CSSStyler;
import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.core.utility.MiscUtil;
import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.Data;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.model.EmployeeModel;
import io.jacobking.quickticket.gui.model.TicketCategoryModel;
import io.jacobking.quickticket.gui.model.TicketModel;
import io.jacobking.quickticket.gui.utility.IconLoader;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import java.net.URL;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TicketController extends Controller {

    private TicketBridge ticketBridge;

    @FXML private TableView<TicketModel>                 ticketTable;
    @FXML private TableColumn<TicketModel, PriorityType> indicatorColumn;
    @FXML private TableColumn<TicketModel, Void>         actionsColumn;
    @FXML private TableColumn<TicketModel, Integer>      categoryColumn;
    @FXML private TableColumn<TicketModel, String>       titleColumn;
    @FXML private TableColumn<TicketModel, PriorityType> priorityColumn;
    @FXML private TableColumn<TicketModel, StatusType>   statusColumn;
    @FXML private TableColumn<TicketModel, String>       employeeColumn;
    @FXML private TableColumn<TicketModel, String>       createdColumn;

    @FXML private Pane openPane;
    @FXML private Pane activePane;
    @FXML private Pane pausedPane;
    @FXML private Pane resolvedPane;

    @FXML private Label openLabel;
    @FXML private Label activeLabel;
    @FXML private Label pausedLabel;
    @FXML private Label resolvedLabel;

    @FXML private Button createButton;
    @FXML private Button openButton;
    @FXML private Button reloadButton;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ticketBridge = bridgeContext.getTicket();

        configureTable();
        configureButtons();
        configureTicketPanels();
    }

    private void configureTable() {
        ticketTable.setItems(ticketBridge.getObservableList().sorted(Comparator.comparing(TicketModel::getCreation).reversed()));
        ticketTable.setOnMouseClicked(event -> {
            final int clickCount = event.getClickCount();
            if (clickCount != 2) {
                return;
            }

            final TicketModel clickedTicket = ticketTable.getSelectionModel().getSelectedItem();
            if (clickedTicket != null) {
                display.show(Route.VIEWER, Data.of(clickedTicket, ticketTable));
            }
        });

        configureIndicatorColumn();
        configureActionsColumn();
        configureCategoryColumn();
        configureTitleColumn();
        configurePriorityColumn();
        configureStatusColumn();
        configureEmployeeColumn();
        configureCreatedColumn();
    }

    private void configureIndicatorColumn() {
        indicatorColumn.setCellValueFactory(data -> data.getValue().priorityProperty());
        indicatorColumn.setCellFactory(data -> new TableCell<>() {
            @Override protected void updateItem(PriorityType priorityType, boolean empty) {
                super.updateItem(priorityType, empty);
                if (priorityType == null || empty) {
                    setGraphic(null);
                    return;
                }

                final Color color = Color.web(getColorCode(priorityType));
                final Label dot = new Label();
                dot.setGraphic(IconLoader.getMaterialIcon(Material2MZ.PRIORITY_HIGH, color));
                setGraphic(dot);
            }

            private String getColorCode(final PriorityType type) {
                return switch (type) {
                    case HIGH -> "#C1292E";
                    case MEDIUM -> "#FFA07A";
                    case LOW -> "#248232";
                };
            }
        });
    }

    private void configureActionsColumn() {
        actionsColumn.setCellFactory(data -> new TableCell<>() {
            private final HBox actionBox = new HBox(3.0);
            private final Button actionOpenButton = new Button();
            private final Button actionDeleteButton = new Button();
            private final Button actionEmailButton = new Button();

            {
                actionOpenButton.setGraphic(IconLoader.getMaterialIcon(
                        IconLoader.getMaterialIcon(Material2MZ.OPEN_IN_BROWSER)));
                actionOpenButton.setTooltip(new Tooltip("Open Ticket"));

                actionEmailButton.setGraphic(IconLoader.getMaterialIcon(
                        IconLoader.getMaterialIcon(Material2AL.EMAIL)));
                actionEmailButton.setTooltip(new Tooltip("E-mail Ticket Employees"));

                actionDeleteButton.setGraphic(IconLoader.getMaterialIcon(
                        IconLoader.getMaterialIcon(Material2AL.DELETE_FOREVER)));
                actionDeleteButton.setTooltip(new Tooltip("Delete Ticket"));
                actionDeleteButton.getStyleClass().add("button-warning");

                actionBox.getChildren().addAll(actionOpenButton, actionEmailButton, actionDeleteButton);
                actionBox.setAlignment(Pos.CENTER);
            }

            @Override protected void updateItem(Void unused, boolean empty) {
                super.updateItem(unused, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }

                final TicketModel targetTicket = getTableRow() == null ? null : getTableRow().getItem();
                if (targetTicket != null) {
                    actionOpenButton.setOnAction(event -> onOpenTicket(targetTicket));
                    actionDeleteButton.setOnAction(event -> onDeleteTicket(targetTicket));
                    actionEmailButton.setOnAction(event -> onEmail(targetTicket));
                }

                setGraphic(actionBox);
            }

            private void onEmail(final TicketModel ticketModel) {
                final int ticketId = ticketModel.getId();
                final ObservableList<EmployeeModel> attachedEmployees = bridgeContext.getTicketEmployee()
                        .getEmployeeModelsForTicket(ticketId);

                if (attachedEmployees.isEmpty()) {
                    Announcements.get().showError("Error", "Could not e-mail employee(s).", "There are none attached.");
                    return;
                }

                final String emailChain = attachedEmployees.stream()
                        .map(EmployeeModel::getEmail)
                        .filter(Objects::nonNull)
                        .filter(email -> !email.isEmpty())
                        .collect(Collectors.joining(";"));

                if (emailChain.isEmpty()) {
                    Announcements.get().showError("Error", "Could not e-mail employee(s).", "There were no e-mails attached.");
                    return;
                }

                final String ticketTitle = ticketModel.getTitle().replaceAll("\\s+", "%20");
                final String emailLink = String.format("mailto:%s?subject=%s", emailChain, ticketTitle);
                MiscUtil.openLink(emailLink);
            }
        });
    }

    private void configureCategoryColumn() {
        categoryColumn.setCellValueFactory(data -> data.getValue().categoryProperty().asObject());
        categoryColumn.setCellFactory(data -> new TableCell<>() {
            @Override protected void updateItem(Integer categoryId, boolean empty) {
                super.updateItem(categoryId, empty);
                if (categoryId == null || empty) {
                    setGraphic(null);
                    return;
                }

                final TicketCategoryModel category = bridgeContext.getCategory().getModel(categoryId);
                if (category == null) {
                    setGraphic(null);
                    return;
                }

                final Label categoryLabel = new Label(category.getNameProperty());
                final String style = CSSStyler.create("-fx-font-weight: bolder")
                        .appendStyle("-fx-font-size: 1em")
                        .toStyle();

                categoryLabel.setStyle(style);
                categoryLabel.setTextFill(Color.web(category.getColorProperty()));
                setGraphic(categoryLabel);
            }
        });
    }

    private void configureTitleColumn() {
        titleColumn.setCellValueFactory(data -> data.getValue().titleProperty());
    }

    private void configurePriorityColumn() {
        priorityColumn.setCellValueFactory(data -> data.getValue().priorityProperty());
        priorityColumn.setCellFactory(data -> new TableCell<>() {
            final SearchableComboBox<PriorityType> priorityComboBox = new SearchableComboBox<>(PriorityType.asObservableList());

            {
                priorityComboBox.setOnAction(event -> {
                    if (getTableRow() != null) {
                        final TicketModel ticket = getTableRow().getItem();
                        if (ticket != null) {
                            ticketTable.getSelectionModel().select(ticket);
                        }
                    }
                });
            }

            @Override protected void updateItem(PriorityType priorityType, boolean empty) {
                super.updateItem(priorityType, empty);
                if (priorityType == null || empty) {
                    setGraphic(null);
                    return;
                }

                final TicketModel ticket = getTableRow().getItem();
                if (ticket == null) {
                    setGraphic(null);
                    return;
                }
                configurePriorityBox(priorityComboBox);
                configurePriorityBoxListener(priorityComboBox);
                priorityComboBox.getSelectionModel().select(priorityType);
                setGraphic(priorityComboBox);
            }

            private void configurePriorityBox(final SearchableComboBox<PriorityType> priorityComboBox) {
                priorityComboBox.setConverter(new StringConverter<>() {
                    @Override public String toString(PriorityType priorityType) {
                        return (priorityType == null) ? "" : priorityType.name();
                    }

                    @Override public PriorityType fromString(String s) {
                        throw new UnsupportedOperationException();
                    }
                });

                priorityComboBox.setPrefHeight(25);
                priorityComboBox.setMinHeight(25);
                priorityComboBox.setMaxHeight(25);
            }

            // TODO: Will eventually need to handle more cases when we add the filters back.
            private void configurePriorityBoxListener(SearchableComboBox<PriorityType> priorityComboBox) {
                priorityComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldPriority, newPriority) -> {
                    if (getTableRow() == null || !getTableRow().isSelected())
                        return;

                    if (newPriority == null) {
                        return;
                    }

                    if (newPriority == oldPriority) {
                        return;
                    }

                    final TicketModel ticket = getTableRow().getItem();
                    if (ticket != null) {
                        ticket.priorityProperty().setValue(newPriority);
                        onUpdateTicket(ticket);
                    }
                });
            }
        });
    }

    private void configureStatusColumn() {
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());
        statusColumn.setCellFactory(data -> new TableCell<>() {
            final SearchableComboBox<StatusType> statusComboBox = new SearchableComboBox<>(StatusType.asObservableList());

            {
                if (getTableRow() != null) {
                    final TicketModel ticket = getTableRow().getItem();
                    if (ticket != null) {
                        ticketTable.getSelectionModel().select(ticket);
                    }
                }
            }

            @Override protected void updateItem(StatusType statusType, boolean empty) {
                super.updateItem(statusType, empty);
                if (statusType == null || empty) {
                    setGraphic(null);
                    return;
                }

                final TicketModel ticket = getTableRow().getItem();
                if (ticket == null) {
                    setGraphic(null);
                    return;
                }

                configureStatusBox(statusComboBox);
                configureStatusBoxListener(statusComboBox);

                statusComboBox.getSelectionModel().select(statusType);
                setGraphic(statusComboBox);
            }

            private void configureStatusBox(final SearchableComboBox<StatusType> statusComboBox) {
                statusComboBox.setConverter(new StringConverter<>() {
                    @Override public String toString(StatusType statusType) {
                        return (statusType == null) ? "" : statusType.name();
                    }

                    @Override public StatusType fromString(String s) {
                        throw new UnsupportedOperationException();
                    }
                });

                statusComboBox.setPrefHeight(25);
                statusComboBox.setMinHeight(25);
                statusComboBox.setMaxHeight(25);
            }


            // TODO: Will eventually need to handle more cases when we add the filters back.
            private void configureStatusBoxListener(final SearchableComboBox<StatusType> statusComboBox) {
                statusComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldStatus, newStatus) -> {
                    if (getTableRow() == null || !getTableRow().isSelected()) {
                        return;
                    }

                    if (newStatus == null) {
                        return;
                    }

                    if (newStatus == oldStatus) {
                        return;
                    }

                    final TicketModel ticket = getTableRow().getItem();
                    if (ticket != null) {
                        ticket.oldStatusProperty().setValue(oldStatus);
                        ticket.statusProperty().setValue(newStatus);
                        onUpdateTicket(ticket);
                    }
                });
            }


        });
    }

    private void configureEmployeeColumn() {
        employeeColumn.setCellFactory(data -> new TableCell<>() {
            @Override protected void updateItem(String string, boolean empty) {
                super.updateItem(string, empty);
                final TicketModel ticket = getTableRow().getItem();
                if (ticket == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                final int ticketId = ticket.getId();
                final ObservableList<EmployeeModel> employeeList = bridgeContext.getTicketEmployee()
                        .getEmployeeModelsForTicket(ticketId);

                if (employeeList.isEmpty()) {
                    final Button addEmployeeButton = new Button("Add Employee(s)");
                    addEmployeeButton.setOnAction(event -> display.show(Route.FIND_EMPLOYEE));
                    return;
                }

                final String names = employeeList.stream()
                        .map(EmployeeModel::getFullName)
                        .collect(Collectors.joining(", "));

                setText(names);
                setGraphic(null);
            }
        });
    }

    private void configureCreatedColumn() {
        createdColumn.setCellValueFactory(data -> new SimpleStringProperty(
                DateUtil.formatDateTime(DateUtil.DateFormat.DATE_TIME_ONE, data.getValue().getCreation())
        ));
    }

    private void configureButtons() {
        openButton.setGraphic(IconLoader.getMaterialIcon(Material2MZ.OPEN_IN_BROWSER));
        openButton.disableProperty().bind(ticketTable.getSelectionModel().selectedItemProperty().isNull());
        openButton.setOnAction(event -> onOpenTicket(ticketTable.getSelectionModel().getSelectedItem()));

        createButton.setGraphic(IconLoader.getMaterialIcon(Material2AL.CREATE));
        createButton.setOnAction(event -> display.show(Route.TICKET_CREATOR, Data.of(ticketTable)));

        reloadButton.setGraphic(IconLoader.getMaterialIcon(Material2MZ.REFRESH));
        reloadButton.setOnAction(event -> {
            ticketTable.refresh();
            Announcements.get().show("Success", "Ticket Table Refreshed");
        });
    }

    private void onOpenTicket(final TicketModel ticketModel) {
        if (ticketModel == null) {
            Announcements.get().showError("Error", "Could not open ticket.", "Ticket not found.");
            return;
        }

        display.show(Route.VIEWER, Data.of(ticketModel, ticketTable));
    }

    private void onDeleteTicket(final TicketModel ticketModel) {
        if (ticketModel == null) {
            Announcements.get().showError("Error", "Could not delete ticket.", "Ticket not found.");
            return;
        }

        Announcements.get().showConfirmation(() -> deleteTicket(ticketModel),
                        "Are you sure you want to delete this ticket?", "It cannot be recovered.")
                .ifPresent(type -> {
                    if (type == ButtonType.YES) {
                        deleteTicket(ticketModel);
                    }
                });
    }

    private void deleteTicket(final TicketModel ticketModel) {
        final boolean deleted = bridgeContext.getTicket().remove(ticketModel.getId());
        if (!deleted) {
            Announcements.get().showError("Error", "Could not delete ticket.", "Please try again.");
        } else {
            Announcements.get().showConfirm("Success", "Ticket deleted.");
        }
    }

    private void onUpdateTicket(final TicketModel ticketModel) {
        if (ticketModel == null) {
            Announcements.get().showError("Error", "Could not update ticket.", "Ticket not found.");
            return;
        }

        if (!ticketBridge.update(ticketModel)) {
            Announcements.get().showError("Error", "Could not update ticket.", "Update failed.");
        }
    }

    private void onOpenCategories() {

    }

    private void configureTicketPanels() {
        openLabel.textProperty().bind(Bindings.size(ticketBridge.getOpenTickets()).asString());
        activeLabel.textProperty().bind(Bindings.size(ticketBridge.getActiveTickets()).asString());
        resolvedLabel.textProperty().bind(Bindings.size(ticketBridge.getResolvedTickets()).asString());
        pausedLabel.textProperty().bind(Bindings.size(ticketBridge.getPausedTickets()).asString());
    }
}
