package io.jacobking.quickticket.gui.controller.impl.ticket;

import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.gui.alert.Notify;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.data.DataRelay;
import io.jacobking.quickticket.gui.model.impl.EmployeeModel;
import io.jacobking.quickticket.gui.model.impl.TicketModel;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.utility.FALoader;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.net.URL;
import java.util.ResourceBundle;

public class TicketController extends Controller {

    private final FilteredList<TicketModel> open     = ticket.getFilteredList(ticketModel -> ticketModel.statusProperty().getValue() == StatusType.OPEN);
    private final FilteredList<TicketModel> active   = ticket.getFilteredList(ticketModel -> ticketModel.statusProperty().getValue() == StatusType.ACTIVE);
    private final FilteredList<TicketModel> paused   = ticket.getFilteredList(ticketModel -> ticketModel.statusProperty().getValue() == StatusType.PAUSED);
    private final FilteredList<TicketModel> resolved = ticket.getFilteredList(ticketModel -> ticketModel.statusProperty().getValue() == StatusType.RESOLVED);


    @FXML private TableView<TicketModel>                 ticketTable;
    @FXML private TableColumn<TicketModel, PriorityType> indicatorColumn;
    @FXML private TableColumn<TicketModel, Void>         actionsColumn;
    @FXML private TableColumn<TicketModel, String>       titleColumn;
    @FXML private TableColumn<TicketModel, StatusType>   statusColumn;
    @FXML private TableColumn<TicketModel, PriorityType> priorityColumn;
    @FXML private TableColumn<TicketModel, Integer>      employeeColumn;
    @FXML private TableColumn<TicketModel, String>       createdColumn;
    @FXML private Label                                  openLabel;
    @FXML private Label                                  activeLabel;
    @FXML private Label                                  pausedLabel;
    @FXML private Label                                  resolvedLabel;
    @FXML private Button                                 filterButton;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTable();
        configureLabels();
    }

    private void configureTable() {
        handleIndicatorColumn();
        handleActionsColumn();
        titleColumn.setCellValueFactory(data -> data.getValue().titleProperty());
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());
        priorityColumn.setCellValueFactory(data -> data.getValue().priorityProperty());
        employeeColumn.setCellValueFactory(data -> data.getValue().employeeProperty().asObject());
        employeeColumn.setCellFactory(data -> new TableCell<>() {
            @Override protected void updateItem(Integer integer, boolean b) {
                super.updateItem(integer, b);
                if (integer == null || b) {
                    setText(null);
                    return;
                }
                final EmployeeModel model = employee.getModel(integer);
                if (model == null) {
                    setText(null);
                    return;
                }
                setText(model.getFullName());
            }
        });

        createdColumn.setCellValueFactory(data -> data.getValue().createdProperty());
        ticketTable.setItems(ticket.getObservableList());
    }

    private void handleIndicatorColumn() {
        indicatorColumn.setCellValueFactory(data -> data.getValue().priorityProperty());
        indicatorColumn.setCellFactory(data -> new TableCell<>() {

            private final Glyph glyph = FALoader.create(FontAwesome.Glyph.CIRCLE, null);
            private final Label indicator = new Label();

            @Override protected void updateItem(PriorityType priorityType, boolean b) {
                super.updateItem(priorityType, b);
                if (b) {
                    setGraphic(null);
                    return;
                }

                if (priorityType == PriorityType.HIGH) {
                    indicator.setGraphic(glyph.color(Color.valueOf("#C1292E")));
                } else if (priorityType == PriorityType.MEDIUM) {
                    indicator.setGraphic(glyph.color(Color.valueOf("#FFF200")));
                } else if (priorityType == PriorityType.LOW) {
                    indicator.setGraphic(glyph.color(Color.valueOf("#248232")));
                }

                setGraphic(indicator);
            }
        });
    }

    private void configureLabels() {
        openLabel.setText(String.valueOf(open.size()));
        activeLabel.setText(String.valueOf(active.size()));
        pausedLabel.setText(String.valueOf(paused.size()));
        resolvedLabel.setText(String.valueOf(resolved.size()));

        addListener(openLabel, open);
        addListener(activeLabel, active);
        addListener(pausedLabel, paused);
        addListener(resolvedLabel, resolved);
    }

    private void addListener(final Label label, final FilteredList<TicketModel> filteredList) {
        filteredList.addListener((ListChangeListener<? super TicketModel>) change -> {
            while (change.next()) {
                final int size = filteredList.size();
                label.setText(String.valueOf(size));
            }
        });
    }

    @FXML private void onCreate() {
        Display.show(Route.TICKET_CREATOR, DataRelay.of(ticketTable));
    }

    @FXML private void onManageEmployees() {
        Display.show(Route.EMPLOYEE_MANAGER);
    }

    @FXML private void onFilter() {
        Notify.showInfo("Not implemented.", "Not implemented.", "This feature has not been implement yet.");
    }

    private void onDelete(final TicketModel ticketModel) {
        if (ticketModel == null) {
            Notify.showError("Failed to delete ticket.", "You must select a ticket.", "Please try again.");
            return;
        }

        Notify.showConfirmation("Are you sure you want to delete this ticket?", "This action cannot be undone.").ifPresent(type -> {
            if (type == ButtonType.YES) {
                ticket.remove(ticketModel.getId());
            }
        });
    }

    private void onOpen(final TicketModel ticketModel) {
        if (ticketModel == null) {
            Notify.showError("Failed to open ticket.", "You must select a ticket.", "Please try again.");
            return;
        }

        Display.show(Route.VIEWER, DataRelay.of(ticketModel, ticketTable));
    }

    private void handleActionsColumn() {
        actionsColumn.setCellFactory(ticketModelVoidTableColumn -> new TableCell<>() {
            private final Button open = new Button();
            private final Button delete = new Button();

            private final HBox box = new HBox();

            {
                box.setSpacing(3.0);
                box.setAlignment(Pos.CENTER);

                open.setGraphic(FALoader.createDefault(FontAwesome.Glyph.FOLDER_OPEN_ALT));
                delete.setGraphic(FALoader.createDefault(FontAwesome.Glyph.CLOSE));
                box.getChildren().addAll(open, delete);

                delete.setOnAction(event -> onDelete(getTableRow().getItem()));
                open.setOnAction(event -> onOpen(getTableRow().getItem()));
            }

            @Override protected void updateItem(Void unused, boolean empty) {
                super.updateItem(unused, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(box);
                }
            }
        });
    }


}
