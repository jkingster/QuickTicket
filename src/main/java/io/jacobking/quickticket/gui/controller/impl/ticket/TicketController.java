package io.jacobking.quickticket.gui.controller.impl.ticket;

import io.jacobking.quickticket.core.email.EmailBuilder;
import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.data.DataRelay;
import io.jacobking.quickticket.gui.misc.PopOverBuilder;
import io.jacobking.quickticket.gui.model.impl.EmployeeModel;
import io.jacobking.quickticket.gui.model.impl.TicketCategoryModel;
import io.jacobking.quickticket.gui.model.impl.TicketModel;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.utility.FALoader;
import io.jacobking.quickticket.tables.pojos.Comment;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class TicketController extends Controller {
    private final ObjectProperty<TicketModel> lastViewed = new SimpleObjectProperty<>();

    private final ObservableMap<Pane, ObservableList<TicketModel>> activePaneMap = FXCollections.observableHashMap();

    @FXML private TableView<TicketModel>                  ticketTable;
    @FXML private TableColumn<TicketModel, PriorityType>  indicatorColumn;
    @FXML private TableColumn<TicketModel, Void>          actionsColumn;
    @FXML private TableColumn<TicketModel, Integer>       categoryColumn;
    @FXML private TableColumn<TicketModel, String>        titleColumn;
    @FXML private TableColumn<TicketModel, StatusType>    statusColumn;
    @FXML private TableColumn<TicketModel, PriorityType>  priorityColumn;
    @FXML private TableColumn<TicketModel, Integer>       employeeColumn;
    @FXML private TableColumn<TicketModel, LocalDateTime> createdColumn;
    @FXML private Label                                   openLabel;
    @FXML private Label                                   activeLabel;
    @FXML private Label                                   pausedLabel;
    @FXML private Label                                   resolvedLabel;
    @FXML private Button                                  lastViewButton;

    @FXML private Pane openPane;
    @FXML private Pane activePane;
    @FXML private Pane pausedPane;
    @FXML private Pane resolvedPane;


    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTable();
        configureLabels();

        openPane.setUserData(StatusType.OPEN);
        activePane.setUserData(StatusType.ACTIVE);
        pausedPane.setUserData(StatusType.PAUSED);
        resolvedPane.setUserData(StatusType.RESOLVED);

        openPane.setOnMousePressed(this::togglePane);
        activePane.setOnMousePressed(this::togglePane);
        pausedPane.setOnMousePressed(this::togglePane);
        resolvedPane.setOnMousePressed(this::togglePane);

    }


    private void configureTable() {
        handleIndicatorColumn();
        handleActionsColumn();
        categoryColumn.setCellValueFactory(data -> data.getValue().categoryProperty().asObject());
        categoryColumn.setCellFactory(data -> new TableCell<>() {
            @Override protected void updateItem(Integer integer, boolean b) {
                super.updateItem(integer, b);
                if (integer == null || b) {
                    setGraphic(null);
                    return;
                }

                final TicketCategoryModel model = category.getModel(integer);
                if (model == null) {
                    setGraphic(null);
                    return;
                }

                final Label label = new Label(model.getNameProperty());
                final String color = model.getColorAsRGB();

                label.setStyle(String.format("-fx-text-fill: %s; -fx-font-weight: bolder;", color));
                setGraphic(label);
            }
        });
        titleColumn.setCellValueFactory(data -> data.getValue().titleProperty());
        titleColumn.setSortable(false);
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());
        statusColumn.setSortable(false);
        priorityColumn.setCellValueFactory(data -> data.getValue().priorityProperty());
        priorityColumn.setSortable(false);
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
        employeeColumn.setSortable(false);
        createdColumn.setCellValueFactory(data -> data.getValue().createdProperty());
        createdColumn.setCellFactory(data -> new TableCell<>() {
            @Override protected void updateItem(LocalDateTime localDateTime, boolean b) {
                super.updateItem(localDateTime, b);
                if (b || localDateTime == null) {
                    setText(null);
                    return;
                }

                setText(DateUtil.formatDateTime(DateUtil.DateFormat.DATE_TIME_ONE, localDateTime));
            }
        });


        ticketTable.setItems(ticket.getObservableList());

        createdColumn.setComparator(LocalDateTime::compareTo);
        createdColumn.setSortType(TableColumn.SortType.DESCENDING);
        ticketTable.getSortOrder().clear();
        ticketTable.getSortOrder().add(createdColumn);
        ticketTable.sort();

        final TicketModel lastViewedModel = ticket.getLastViewed();
        if (lastViewedModel != null) {
            lastViewed.setValue(lastViewedModel);
        }

        lastViewButton.disableProperty().bind(lastViewed.isNull());
        handleDoubleClick();
    }

    private void handleDoubleClick() {
        ticketTable.setRowFactory(ticketModelTableView -> {
            final TableRow<TicketModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    final TicketModel ticket = row.getItem();
                    openTicket(ticket);
                }
            });
            return row;
        });
    }

    private void openTicket(final TicketModel ticketModel) {
        if (ticketModel == null) {
            Announcements.get().showError("Failed", "Could not open ticket.", "Please try again.");
            return;
        }

        Display.show(Route.VIEWER, DataRelay.of(ticketModel, ticketTable, lastViewed));
    }

    private void handleIndicatorColumn() {
        indicatorColumn.setCellValueFactory(data -> data.getValue().priorityProperty());
        indicatorColumn.setSortable(false);
        indicatorColumn.setReorderable(false);
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
                    indicator.setGraphic(glyph.color(Color.valueOf("#5DADD5")));
                    setGraphic(indicator);
                    return;
                }

                if (priorityType == PriorityType.HIGH) {
                    indicator.setGraphic(glyph.color(Color.ORANGE));
                } else if (priorityType == PriorityType.MEDIUM) {
                    indicator.setGraphic(glyph.color(Color.YELLOW));
                } else if (priorityType == PriorityType.LOW) {
                    indicator.setGraphic(glyph.color(Color.GREEN));
                }

                setGraphic(indicator);
            }
        });
    }

    private void configureLabels() {
        openLabel.setText(String.valueOf(getListByStatus(StatusType.OPEN).size()));
        activeLabel.setText(String.valueOf(getListByStatus(StatusType.ACTIVE).size()));
        pausedLabel.setText(String.valueOf(getListByStatus(StatusType.PAUSED).size()));
        resolvedLabel.setText(String.valueOf(getListByStatus(StatusType.RESOLVED).size()));

        addListener(openLabel, getListByStatus(StatusType.OPEN));
        addListener(activeLabel, getListByStatus(StatusType.ACTIVE));
        addListener(pausedLabel, getListByStatus(StatusType.PAUSED));
        addListener(resolvedLabel, getListByStatus(StatusType.RESOLVED));
    }

    public TableView<TicketModel> getTicketTable() {
        return ticketTable;
    }

    private void addListener(final Label label, final ObservableList<TicketModel> targetList) {
        targetList.addListener((ListChangeListener<? super TicketModel>) change -> {
            while (change.next()) {
                final int size = targetList.size();
                label.setText(String.valueOf(size));
            }
        });
    }

    @FXML private void onCreate() {
        Display.show(Route.TICKET_CREATOR, DataRelay.of(this));
    }

    @FXML private void onResolve() {
        final TicketModel ticketModel = ticketTable.getSelectionModel().getSelectedItem();
        if (ticketModel == null) {
            Announcements.get().showError("Failed to resolve ticket.", "No ticket was selected.", "Please try again after selecting a ticket.");
            return;
        }
        final StatusType originalStatus = ticketModel.statusProperty().getValue();
        ticketModel.statusProperty().setValue(StatusType.RESOLVED);

        if (ticket.update(ticketModel, originalStatus)) {
            promptNotifyEmployeeAlert(ticketModel);
        }
    }

    private void promptNotifyEmployeeAlert(final TicketModel viewedTicket) {
        Announcements.get().showInput("This ticket has been marked resolved.", "Would you like to notify the employee? Please provide any closing comments.")
                .ifPresent(pair -> {
                    final ButtonType type = pair.getLeft();
                    final String comment = pair.getRight();
                    if (type == ButtonType.YES) {
                        postComment(viewedTicket, comment);
                        processNotificationEmail(viewedTicket, comment);
                    } else if (type == ButtonType.NO) {
                        postComment(viewedTicket, comment);
                    } else if (type == ButtonType.CANCEL) {
                        postFailureComment(viewedTicket, "Notification not sent and resolving comment unknown.");
                    }
                });
    }

    private void processNotificationEmail(final TicketModel viewedTicket, final String resolvingComment) {
        final EmployeeModel employee = getEmployee();
        if (employee == null) {
            Announcements.get().showError("Failed to retrieve record.", "Employee could not be fetched from database.", "Please attach an employee.");
            postFailureComment(viewedTicket, "Could not retrieve employee record.");
            return;
        }

        final String employeeEmail = employee.getEmail();
        if (employeeEmail.isEmpty()) {
            Announcements.get().showError("Failed to retrieve e-mail.", "Could not notify employee.", "PLease attach an e-mail to employees' record.");
            postFailureComment(viewedTicket, "Could not retrieve employee e-mail.");
            return;
        }

        notifyEmployee(viewedTicket, employeeEmail, resolvingComment, employee);
    }

    private void postEmptyResolvingComment(final TicketModel viewedTicket) {
        postComment(viewedTicket, "No resolving comment.");
    }

    private void postFailureComment(final TicketModel ticketModel, final String comment) {
        postComment(ticketModel, comment);
    }

    private void notifyEmployee(final TicketModel viewedTicket, final String email, final String resolvingComment, final EmployeeModel employeeModel) {
        new EmailBuilder(email, EmailBuilder.EmailType.RESOLVED)
                .format(
                        viewedTicket.getId(),
                        viewedTicket.getTitle(),
                        DateUtil.formatDateTime(DateUtil.DateFormat.DATE_TIME_ONE, viewedTicket.getCreation()),
                        employeeModel.getFullName(),
                        resolvingComment
                )
                .email(emailConfig)
                .setSubject(getSubject(viewedTicket))
                .sendEmail();
    }

    private String getSubject(final TicketModel ticketModel) {
        return String.format("Your support ticket has been resolved. | Ticket ID: %s", ticketModel.getId());
    }

    private void postComment(final TicketModel ticket, final String commentText) {
        if (commentText.isEmpty()) {
            postEmptyResolvingComment(ticket);
            return;
        }

        comment.createModel(new Comment()
                .setTicketId(ticket.getId())
                .setPost(commentText)
                .setPostedOn(DateUtil.nowAsLocalDateTime(DateUtil.DateFormat.DATE_TIME_ONE))
        );
    }

    private EmployeeModel getEmployee() {
        final TicketModel ticketModel = ticketTable.getSelectionModel().getSelectedItem();
        if (ticketModel == null)
            return null;
        return employee.getModel(ticketModel.getEmployeeId());
    }

    @FXML private void onReopen() {
        final TicketModel ticketModel = ticketTable.getSelectionModel().getSelectedItem();
        if (ticketModel == null) {
            Announcements.get().showError("Failed to re-open ticket.", "No ticket was selected.", "Please try again after selecting a ticket.");
            return;
        }

        final StatusType originalStatus = ticketModel.statusProperty().getValue();
        ticketModel.statusProperty().setValue(StatusType.OPEN);
        if (ticket.update(ticketModel, originalStatus)) {
            Announcements.get().showInfo("Success", "Ticket re-opened successfully.");
            ticketTable.refresh();
        }
    }

    @FXML private void onRefresh() {
        ticketTable.refresh();
    }

    @FXML private void onOpenLastViewed() {
        Display.show(Route.VIEWER, DataRelay.of(lastViewed.getValue(), ticketTable, lastViewed));
    }


    private void onDelete(final TicketModel ticketModel) {
        if (ticketModel == null) {
            Announcements.get().showError("Failed to delete ticket.", "You must select a ticket.", "Please try again.");
            return;
        }

        Announcements.get().showConfirmation(() -> removeTicket(ticketModel), "Are you sure you want to delete this ticket?", "This action cannot be undone.")
                .ifPresent(type -> {
                    if (type == ButtonType.YES) {
                        removeTicket(ticketModel);
                    }
                });
    }

    private void removeTicket(final TicketModel ticketModel) {
        ticket.remove(ticketModel.getId());
        final TicketModel lastViewedModel = lastViewed.getValue();
        if (lastViewedModel != null) {
            final int ticketId = ticketModel.getId();
            if (ticketId == lastViewedModel.getId()) {
                lastViewed.setValue(null);
            }
        }
        setTicketTable();
    }

    private void onOpen(final TicketModel ticketModel) {
        if (ticketModel == null) {
            Announcements.get().showError("Failed to open ticket.", "You must select a ticket.", "Please try again.");
            return;
        }

        Display.show(Route.VIEWER, DataRelay.of(ticketModel, ticketTable, lastViewed));
    }

    private void onEmail(final TicketModel ticketModel) {
        if (ticketModel == null) {
            Announcements.get().showError("Failed to open ticket.", "You must select a ticket.", "Please try again.");
            return;
        }

        final Desktop desktop = Desktop.getDesktop();
        if (!Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.MAIL)) {
            Announcements.get().showError("Failed to open mail.", "Failed to open mail app.", "Mailing is not supported.");
            return;
        }

        final EmployeeModel model = employee.getModel(ticketModel.getEmployeeId());
        if (model == null) {
            Announcements.get().showError("Failed to open mail.", "There is no employee attached to this ticket.", "Please set an employee and try again.");
            return;
        }

        final String employeeEmail = model.getEmail();
        if (employeeEmail.isEmpty()) {
            Announcements.get().showError("Failed to open mail.", "There is no e-mail to this employee.", "Please set an e-mail and try again.");
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
            Announcements.get().showException("Failed to open mail app.", e.fillInStackTrace());
        }
    }

    private void handleActionsColumn() {
        actionsColumn.setReorderable(false);
        actionsColumn.setSortable(false);
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

    private void togglePane(final MouseEvent event) {
        if (event.getSource() instanceof Pane pane) {
            if (activePaneMap.containsKey(pane)) {
                deactivatePane(pane);
            } else {
                activatePane(pane);
            }
        }
    }


    private void activatePane(final Pane pane) {
        final StatusType statusType = (StatusType) pane.getUserData();
        final ObservableList<TicketModel> targetList = getListByStatus(statusType);
        activePaneMap.put(pane, targetList);
        highlightPane(pane);
        setTicketTable();
    }

    private void deactivatePane(final Pane pane) {
        activePaneMap.remove(pane);
        removePaneHighlight(pane);
        if (activePaneMap.isEmpty()) {
            ticketTable.setItems(ticket.getObservableList());
            ticketTable.refresh();
            return;
        }

        setTicketTable();
    }

    private ObservableList<TicketModel> getListByStatus(final StatusType type) {
        return ticket.getListByStatus(type);
    }

    private void highlightPane(final Pane pane) {
        pane.setStyle("-fx-background-color: #5DADD5;");
    }

    private void removePaneHighlight(final Pane pane) {
        pane.setStyle("-fx-background-color: derive(-fx-primary, 25%);");
    }


    public void setTicketTable() {
        if (activePaneMap.isEmpty()) {
            ticketTable.setItems(ticket.getObservableList());
            ticketTable.refresh();
            return;
        }

        final ObservableList<TicketModel> mergedList = FXCollections.observableArrayList();

        for (final ObservableList<TicketModel> targetList : activePaneMap.values()) {
            mergedList.addAll(targetList);
        }

        ticketTable.setItems(mergedList);
        ticketTable.refresh();
    }

    @FXML private Button categoriesButton;

    @FXML private void onCategories() {
        final PopOverBuilder popOverBuilder = new PopOverBuilder()
                .setArrowOrientation(PopOver.ArrowLocation.BOTTOM_RIGHT)
                .setTitle("Ticket Categories")
                .useDefaultSettings()
                .setContent(getCategoryNode())
                .setOwner(categoriesButton);
        popOverBuilder.show();
    }

    private VBox getCategoryNode() {
        final SearchableComboBox<TicketCategoryModel> comboBox = new SearchableComboBox<>(category.getObservableList());
        configureComboBox(comboBox);

        comboBox.setMinWidth(180);
        final VBox vBox = new VBox(5.0);
        vBox.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                comboBox.getSelectionModel().clearSelection();
            }
        });
        vBox.setPrefWidth(200);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.CENTER);

        final HBox hBox = new HBox(5.0);
        final Button newButton = new Button();
        newButton.setGraphic(FALoader.createDefault(FontAwesome.Glyph.PLUS_SQUARE));
        newButton.setOnAction(event -> Display.show(Route.CATEGORY_CREATOR));
        newButton.disableProperty().bind(comboBox.getSelectionModel().selectedItemProperty().isNotNull());

        final Button editButton = new Button();
        editButton.setGraphic(FALoader.createDefault(FontAwesome.Glyph.EDIT));
        editButton.disableProperty().bind(comboBox
                .getSelectionModel()
                .selectedItemProperty()
                .isEqualTo(category.getModel(0))
        );

        editButton.setOnAction(event -> {
            final TicketCategoryModel selected = comboBox.getSelectionModel().getSelectedItem();
            if (selected == null) {
                Announcements.get().showError("Failure.", "Could not edit category.", "Please select one!");
                return;
            }
            Display.show(Route.CATEGORY_CREATOR, DataRelay.of(selected));
        });

        final Button deleteButton = new Button();
        deleteButton.setGraphic(FALoader.createDefault(FontAwesome.Glyph.CLOSE));
        deleteButton.disableProperty().bind(comboBox
                .getSelectionModel()
                .selectedItemProperty()
                .isEqualTo(category.getModel(0))
        );

        deleteButton.setOnAction(event -> {
            final TicketCategoryModel selected = comboBox.getSelectionModel().getSelectedItem();
            if (selected == null) {
                Announcements.get().showError("Failure.", "Could not delete category.", "Please select one!");
                return;
            }

            confirmCategoryDeletion(selected);
        });


        final Button clearButton = new Button();
        clearButton.setGraphic(FALoader.createDefault(FontAwesome.Glyph.ERASER));
        clearButton.setOnAction(event -> comboBox.getSelectionModel().clearSelection());

        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(newButton, editButton, deleteButton, clearButton);

        vBox.getChildren().addAll(comboBox, hBox);
        return vBox;
    }

    private void confirmCategoryDeletion(final TicketCategoryModel model) {
        Announcements.get().showConfirmation(() -> deleteCategory(model),
                "Are you sure you want to delete this category?",
                "It cannot be recovered once deleted."
        ).ifPresent(type -> {
            if (type == ButtonType.YES) {
                deleteCategory(model);
            }
        });
    }

    private void deleteCategory(final TicketCategoryModel model) {
        category.remove(model.getId());
    }

    private void configureComboBox(final SearchableComboBox<TicketCategoryModel> comboBox) {
        comboBox.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(TicketCategoryModel ticketCategoryModel, boolean b) {
                super.updateItem(ticketCategoryModel, b);
                if (ticketCategoryModel == null || b) {
                    setText(null);
                    return;
                }

                final String description = ticketCategoryModel.getDescriptionProperty().isEmpty()
                                           ? "No description provided."
                                           : ticketCategoryModel.getDescriptionProperty();

                setText(String.format("%s - %s", ticketCategoryModel.getNameProperty(), description));
            }
        });

        comboBox.setConverter(new StringConverter<>() {
            @Override public String toString(TicketCategoryModel ticketCategoryModel) {
                if (ticketCategoryModel == null)
                    return "";

                final String description = ticketCategoryModel.getDescriptionProperty().isEmpty()
                                           ? "No description provided."
                                           : ticketCategoryModel.getDescriptionProperty();
                return String.format("%s - %s", ticketCategoryModel.getNameProperty(), description);
            }

            @Override public TicketCategoryModel fromString(String s) {
                return null;
            }
        });
    }
}
