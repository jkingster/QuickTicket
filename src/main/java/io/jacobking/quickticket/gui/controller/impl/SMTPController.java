package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.email.EmailConfig;
import io.jacobking.quickticket.core.email.EmailSender;
import io.jacobking.quickticket.core.type.TransportType;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.model.impl.EmailModel;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.tables.pojos.Email;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SMTPController extends Controller {

    @FXML
    private TextField               hostField;
    @FXML
    private TextField               portField;
    @FXML
    private TextField               fromAddressField;
    @FXML
    private ComboBox<TransportType> transportComboBox;
    @FXML
    private CheckBox                authenticationCheckBox;
    @FXML
    private TextField               usernameField;
    @FXML
    private TextField               passwordField;
    @FXML
    private Button                  saveButton;

    @FXML
    private TextField testAddressField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        preloadValues();
        configureComboBox();
        usernameField.disableProperty().bind(authenticationCheckBox.selectedProperty().not());
        passwordField.disableProperty().bind(usernameField.disableProperty());

        saveButton.disableProperty().bind(hostField.textProperty().isEmpty()
                .or(fromAddressField.textProperty().isEmpty()));
    }

    private void preloadValues() {
        final Email email = EmailConfig.getInstance().getEmail();
        hostField.setText(email.getHost());
        portField.setText(email.getPort());
        fromAddressField.setText(email.getFromAddress());

        if (email.getStarttls()) {
            transportComboBox.getSelectionModel().select(TransportType.STARTTLS);
        } else {
            transportComboBox.getSelectionModel().select(TransportType.SSL_OR_TSL);
        }

        authenticationCheckBox.setSelected(email.getAuthentication());
        usernameField.setText(email.getUsername());
        passwordField.setText(email.getPassword());
    }

    @FXML
    private void onTest() {
        final String testEmail = testAddressField.getText();
        if (testEmail.isEmpty())
            return;

        EmailConfig.getInstance()
                .setEmail(getEmail())
                .applySettings();

        final EmailSender sender = new EmailSender(EmailConfig.getInstance());
        sender.sendEmail("QuickTicket SMTP Test", testEmail, "This email can be ignored.");
    }

    @FXML
    private void onSave() {
        email.update(new EmailModel(getEmail()));
        Display.close(Route.SMTP);
    }

    private Email getEmail() {
        return new Email()
                .setId(0)
                .setHost(hostField.getText())
                .setPort(portField.getText().isEmpty() ? "25" : portField.getText())
                .setAuthentication(authenticationCheckBox.isSelected())
                .setOverSslOrTls(
                        transportComboBox.getSelectionModel().getSelectedItem()
                                == TransportType.SSL_OR_TSL
                )
                .setStarttls(
                        transportComboBox.getSelectionModel().getSelectedItem()
                                == TransportType.STARTTLS
                )
                .setUsername(usernameField.getText())
                .setPassword(passwordField.getText())
                .setFromAddress(fromAddressField.getText())
                .setPort(passwordField.getText());
    }

    private void configureComboBox() {
        transportComboBox.setItems(FXCollections.observableArrayList(TransportType.values()));
        transportComboBox.setCellFactory(data -> new ListCell<>() {
            @Override
            protected void updateItem(TransportType transportType, boolean b) {
                super.updateItem(transportType, b);
                if (transportType == null || b) {
                    setText(null);
                    return;
                }
                setText(transportType.name());
            }
        });
    }
}
