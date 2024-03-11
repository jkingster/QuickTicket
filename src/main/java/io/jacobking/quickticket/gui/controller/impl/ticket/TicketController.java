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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class TicketController extends Controller {

    private final FilteredList<TicketModel> open     = ticket.getFilteredList(ticketModel -> ticketModel.statusProperty().getValue() == StatusType.OPEN);
    private final FilteredList<TicketModel> active   = ticket.getFilteredList(ticketModel -> ticketModel.statusProperty().getValue() == StatusType.ACTIVE);
    private final FilteredList<TicketModel> paused   = ticket.getFilteredList(ticketModel -> ticketModel.statusProperty().getValue() == StatusType.PAUSED);
    private final FilteredList<TicketModel> resolved = ticket.getFilteredList(ticketModel -> ticketModel.statusProperty().getValue() == StatusType.RESOLVED);

    private final ObjectProperty<TicketModel> lastViewed = new SimpleObjectProperty<>();

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
    @FXML private Button                                 lastViewButton;

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

        final TicketModel lastViewed = ticket.getLastViewed();
        if (lastViewed != null) {
            this.lastViewed.setValue(lastViewed);
        }

        lastViewButton.disableProperty().bind(this.lastViewed.isNull());
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

                final TicketModel ticketModel = getTableRow().getItem();
                if (ticketModel == null) {
                    setGraphic(null);
                    return;
                }

                if (StatusType.of(ticketModel.getStatus()) == StatusType.RESOLVED) {
                    indicator.setGraphic(glyph.color(Color.valueOf("#282B36")));
                    setGraphic(indicator);
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

    @FXML private void onResolve() {
        final TicketModel ticketModel = ticketTable.getSelectionModel().getSelectedItem();
        if (ticketModel == null) {
            Notify.showError("Failed to resolve ticket.", "No ticket was selected.", "Please try again after selecting a ticket.");
            return;
        }
        ticketModel.statusProperty().setValue(StatusType.RESOLVED);
        ticket.update(ticketModel);
    }

    @FXML private void onReopen() {
        final TicketModel ticketModel = ticketTable.getSelectionModel().getSelectedItem();
        if (ticketModel == null) {
            Notify.showError("Failed to re-open ticket.", "No ticket was selected.", "Please try again after selecting a ticket.");
            return;
        }
        ticketModel.statusProperty().setValue(StatusType.OPEN);
        ticket.update(ticketModel);
    }

    @FXML private void onOpenLastViewed() {
        Display.show(Route.VIEWER, DataRelay.of(this.lastViewed.getValue(), ticketTable, this.lastViewed));
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

        Display.show(Route.VIEWER, DataRelay.of(ticketModel, ticketTable, this.lastViewed));
    }

    private void onEmail(final TicketModel ticketModel) {
        if (ticketModel == null) {
            Notify.showError("Failed to open ticket.", "You must select a ticket.", "Please try again.");
            return;
        }

        final Desktop desktop = Desktop.getDesktop();
        if (!Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.MAIL)) {
            Notify.showError("Failed to open mail.", "Failed to open mail app.", "Mailing is not supported.");
            return;
        }

        final EmployeeModel model = employee.getModel(ticketModel.getEmployeeId());
        if (model == null) {
            Notify.showError("Failed to open mail.", "There is no employee attached to this ticket.", "Please set an employee and try again.");
            return;
        }

        final String employeeEmail = model.getEmail();
        if (employeeEmail.isEmpty()) {
            Notify.showError("Failed to open mail.", "There is no e-mail to this employee.", "Please set an e-mail and try again.");
            return;
        }

        attemptToOpenEmailApp(desktop, employeeEmail, ticketModel);
    }

    private void attemptToOpenEmailApp(final Desktop desktop, final String email, final TicketModel ticketModel) {
        try {
            final String subject = String.format("Ticket ID: %d | %s", ticketModel.getId(), ticketModel.getTitle());
            final String uriEncoded = "mailto:" + email + "?subject=" + subject.replaceAll(" ", "%20")
                    .replaceAll("\\|", "%7C");

            final URI uri = new URI(uriEncoded);
            desktop.mail(uri);
        } catch (URISyntaxException | IOException e) {
            Notify.showException("Failed to open mail app.", e.fillInStackTrace());
        }
    }

    private void handleActionsColumn() {
        actionsColumn.setCellFactory(ticketModelVoidTableColumn -> new TableCell<>() {
            private final Button open = new Button();
            private final Button delete = new Button();
            private final Button email = new Button();

            private final HBox box = new HBox();

            {
                box.setSpacing(3.0);
                box.setAlignment(Pos.CENTER);

                open.setGraphic(FALoader.createDefault(FontAwesome.Glyph.FOLDER_OPEN_ALT));
                delete.setGraphic(FALoader.createDefault(FontAwesome.Glyph.CLOSE));
                email.setGraphic(FALoader.createDefault(FontAwesome.Glyph.SEND));
                box.getChildren().addAll(open, delete, email);

                delete.setOnAction(event -> onDelete(getTableRow().getItem()));
                open.setOnAction(event -> onOpen(getTableRow().getItem()));
                email.setOnAction(event -> onEmail(getTableRow().getItem()));
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
