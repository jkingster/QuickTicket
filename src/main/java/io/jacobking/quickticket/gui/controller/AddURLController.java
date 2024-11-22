package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.model.LinkModel;
import io.jacobking.quickticket.gui.model.TicketModel;
import io.jacobking.quickticket.tables.pojos.TicketLink;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AddURLController extends Controller {

    private TicketModel ticket;

    @FXML private TextField ticketIdField;
    @FXML private TextField urlField;
    @FXML private TextArea  descriptionArea;
    @FXML private CheckBox  httpsCheckBox;
    @FXML private Button    addURLButton;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ticket = data.mapIndex(0, TicketModel.class);
        ticketIdField.setText(ticket.getId() + "");
        configureButton();
    }

    private void configureButton() {
        addURLButton.disableProperty().bind(urlField.textProperty().isEmpty().or(checkURLValidity().not()));
    }


    private BooleanBinding checkURLValidity() {
        return new BooleanBinding() {
            {
                super.bind(urlField.textProperty(), httpsCheckBox.selectedProperty());
            }

            @Override protected boolean computeValue() {
                final String url = urlField.getText();

                if (httpsCheckBox.isSelected()) {
                    return isValidHttpsUrl(url);
                }

                return isValidHttpUrl(url);
            }
        };
    }

    private static final String HTTP_REGEX_PATTERN  = "^(http://|https://)\\S+$";
    private static final String HTTPS_REGEX_PATTERN = "^https://\\S+$";

    private boolean isValidHttpUrl(final String url) {
        return url.matches(HTTP_REGEX_PATTERN);
    }

    private boolean isValidHttpsUrl(final String url) {
        return url.matches(HTTPS_REGEX_PATTERN);
    }

    @FXML
    private void onAddURL() {
        if (!httpsCheckBox.isSelected()) {
            Announcements.get().showWarning("Warning", "Unsecure Link", "http is an unsecure protocol. Consider using http(s)!");
        }

        final String processedUrl = getProcessedUrl();
        if (processedUrl == null) {
            Announcements.get().showError("Error", "URL Processing Failure", "Could not properly encode link...");
            return;
        }

        final LinkModel newLink = getNewLink(processedUrl);
        if (newLink == null) {
            Announcements.get().showError("Error", "Failed to create URL attachment.", "Please try again.");
            return;
        }

        Announcements.get().showConfirm("Success", "URL Attachment Created!");
        display.close(Route.ADD_URL);
    }

    @FXML
    private void onReset() {
        descriptionArea.clear();
        httpsCheckBox.setSelected(true);
    }

    @FXML
    private void onCancel() {
        display.close(Route.ADD_URL);
    }

    // Utilities

    private LinkModel getNewLink(final String processedUrl) {
        return bridgeContext.getTicketLink().createModel(new TicketLink()
                .setLink(processedUrl)
                .setTicketId(ticket.getId())
                .setExtension(null)
                .setCreatedOn(DateUtil.formatDateTime(DateUtil.DateFormat.DATE_TIME_ONE, LocalDateTime.now()))
                .setDescription(descriptionArea.getText())
        );
    }

    private String getProcessedUrl() {
        try {
            final String url = urlField.getText();
            final String sanitized = url.replaceAll("\\{", "%7B")
                    .replaceAll("}", "%7D")
                    .replaceAll("\\|", "%7C");
            final URIBuilder uriBuilder = new URIBuilder(sanitized);
            return uriBuilder.build().toString();
        } catch (Exception e) {
            return null;
        }
    }
}
