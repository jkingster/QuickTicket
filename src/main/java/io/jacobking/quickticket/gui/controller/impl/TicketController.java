package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.data.DataRelay;
import io.jacobking.quickticket.gui.model.impl.TicketModel;
import io.jacobking.quickticket.gui.model.impl.UserModel;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.utility.FALoader;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.net.URL;
import java.util.ResourceBundle;

public class TicketController extends Controller {

    @FXML
    private TableView<TicketModel> ticketTable;
    @FXML
    private TableColumn<TicketModel, PriorityType> indicatorColumn;
    @FXML
    private TableColumn<TicketModel, Void> actionsColumn;
    @FXML
    private TableColumn<TicketModel, String> titleColumn;
    @FXML
    private TableColumn<TicketModel, StatusType> statusColumn;
    @FXML
    private TableColumn<TicketModel, PriorityType> priorityColumn;
    @FXML
    private TableColumn<TicketModel, UserModel> userColumn;
    @FXML
    private TableColumn<TicketModel, String> createdColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTable();
    }

    private void configureTable() {
        handleIndicatorColumn();
        handleActionsColumn();
        titleColumn.setCellValueFactory(data -> data.getValue().titleProperty());
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());
        priorityColumn.setCellValueFactory(data -> data.getValue().priorityProperty());
        userColumn.setCellValueFactory(data -> data.getValue().userProperty());
        createdColumn.setCellValueFactory(data -> data.getValue().createdProperty());

        ticketTable.setItems(ticket.getObservableList());
    }

    private void handleIndicatorColumn() {
        indicatorColumn.setCellValueFactory(data -> data.getValue().priorityProperty());
        indicatorColumn.setCellFactory(data -> new TableCell<>() {

            private final Glyph glyph = FALoader.create(FontAwesome.Glyph.CIRCLE, null);
            private final Label indicator = new Label();

            @Override
            protected void updateItem(PriorityType priorityType, boolean b) {
                super.updateItem(priorityType, b);
                if (b) {
                    setGraphic(null);
                    return;
                }

                if (priorityType == PriorityType.HIGH) {
                    indicator.setGraphic(glyph.color(Color.valueOf("#FFA07A")));
                } else {
                    indicator.setGraphic(glyph.color(Color.GREEN));
                }

                setGraphic(indicator);
            }
        });
    }

    private void onDelete(final TicketModel ticketModel) {
        if (ticketModel == null) {
            return;
        }
        ticket.remove(ticketModel.getId());
    }

    private void onOpen(final TicketModel ticketModel) {
        if (ticketModel == null) {
            return;
        }

        Display.show(Route.VIEWER, DataRelay.of(ticketModel));
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

            @Override
            protected void updateItem(Void unused, boolean empty) {
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
