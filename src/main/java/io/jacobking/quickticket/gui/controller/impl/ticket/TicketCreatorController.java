package io.jacobking.quickticket.gui.controller.impl.ticket;

import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.model.impl.TicketModel;
import io.jacobking.quickticket.gui.model.impl.UserModel;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.tables.pojos.Ticket;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TicketCreatorController extends Controller {

    private TableView<TicketModel> ticketTable;

    @FXML
    private ComboBox<StatusType> statusTypeComboBox;

    @FXML
    private ComboBox<PriorityType> priorityTypeComboBox;

    @FXML
    private SearchableComboBox<UserModel> employeeComboBox;

    @FXML
    private TextField titleField;

    @FXML
    private TextArea commentField;

    @FXML
    private Button createButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDataRelay();
        configureStatusComboBox();
        configurePriorityComboBox();
        createButton.disableProperty().bind(titleField.textProperty().isEmpty());
    }

    @SuppressWarnings("unchecked")
    private void setDataRelay() {
        if (dataRelay == null) {
            return;
        }

        dataRelay.mapFirstInto(TableView.class).ifPresentOrElse(tableView -> {
            this.ticketTable = (TableView<TicketModel>) tableView;
        }, () -> {
            throw new RuntimeException("Failed to pass over table view.");
        });
    }

    private void configureStatusComboBox() {
        statusTypeComboBox.setItems(FXCollections.observableArrayList(StatusType.values()));
        statusTypeComboBox.setCellFactory(data -> new ListCell<>() {
            @Override
            protected void updateItem(StatusType statusType, boolean b) {
                super.updateItem(statusType, b);
                if (b || statusType == null) {
                    setText(null);
                    return;
                }

                final String name = statusType.name();
                setText(name);
            }
        });
    }

    private void configurePriorityComboBox() {
        priorityTypeComboBox.setItems(FXCollections.observableArrayList(PriorityType.values()));
        priorityTypeComboBox.setCellFactory(data -> new ListCell<>() {
            @Override
            protected void updateItem(PriorityType priorityType, boolean b) {
                super.updateItem(priorityType, b);
                if (b || priorityType == null) {
                    setText(null);
                    return;
                }

                final String name = priorityType.name();
                setText(name);
            }
        });
    }

    @FXML
    private void onCreate() {
        final String title = titleField.getText();
        ticket.createModel(new Ticket()
                .setTitle(title)
                .setCreatedOn(DateUtil.now())
                .setPriority(getPriority())
                .setStatus(getStatus())
                .setUserId(0)
        );
        Display.close(Route.TICKET_CREATOR);
    }

    @FXML
    private void onReset() {
        titleField.clear();
        commentField.clear();
        priorityTypeComboBox.getSelectionModel().clearSelection();
        statusTypeComboBox.getSelectionModel().clearSelection();
        employeeComboBox.getSelectionModel().clearSelection();
    }

    private String getPriority() {
        final PriorityType type = priorityTypeComboBox.getSelectionModel().getSelectedItem();
        return type == null ? "LOW" : type.name();
    }

    private String getStatus() {
        final StatusType type = statusTypeComboBox.getSelectionModel().getSelectedItem();
        return type == null ? "OPEN" : type.name();
    }
}
