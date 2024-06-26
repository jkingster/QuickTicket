package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.email.EmailBuilder;
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

    @FXML private TextField               hostField;
    @FXML private TextField               portField;
    @FXML private TextField               fromAddressField;
    @FXML private TextField               bccField;
    @FXML private ComboBox<TransportType> transportComboBox;
    @FXML private CheckBox                authenticationCheckBox;
    @FXML private TextField               usernameField;
    @FXML private TextField               passwordField;
    @FXML private Button                  saveButton;

    @FXML private TextField testAddressField;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        preloadValues();
        configureComboBox();
        usernameField.disableProperty().bind(authenticationCheckBox.selectedProperty().not());
        passwordField.disableProperty().bind(usernameField.disableProperty());

        saveButton.disableProperty().bind(hostField.textProperty().isEmpty().or(fromAddressField.textProperty().isEmpty()));
    }

    private void preloadValues() {
        final EmailModel model = new EmailModel(emailConfig.getEmail());
        hostField.setText(model.getHostProperty());
        portField.setText(model.getPortProperty());
        fromAddressField.setText(model.getFromAddressProperty());
        bccField.setText(model.getBccAddress());

        if (model.startTLSProperty().getValue()) {
            transportComboBox.getSelectionModel().select(TransportType.STARTTLS);
        } else {
            transportComboBox.getSelectionModel().select(TransportType.SSL_OR_TSL);
        }

        authenticationCheckBox.setSelected(model.isAuthentication());
        usernameField.setText(model.getUsernameProperty());
        passwordField.setText(model.getPasswordProperty());
    }

    @FXML private void onTest() {
        final String testEmail = testAddressField.getText();
        if (testEmail.isEmpty()) return;

        emailConfig.setEmail(getEmail()).applySettings();

        new EmailBuilder(testEmail, EmailBuilder.EmailType.TEST)
                .email(emailConfig)
                .setSubject("This is a test e-mail from QuickTicket.")
                .sendEmail();
    }

    @FXML private void onSave() {
        final Email savedEmail = getEmail();
        email.update(new EmailModel(savedEmail));
        emailConfig.setEmail(savedEmail);
        Display.close(Route.SMTP);
    }

    private Email getEmail() {
        return new Email().setId(0)
                .setHost(hostField.getText())
                .setPort(portField.getText().isEmpty() ? "25" : portField.getText())
                .setAuthentication(authenticationCheckBox.isSelected())
                .setOverSslOrTls(transportComboBox.getSelectionModel().getSelectedItem() == TransportType.SSL_OR_TSL)
                .setStarttls(transportComboBox.getSelectionModel().getSelectedItem() == TransportType.STARTTLS)
                .setUsername(usernameField.getText())
                .setPassword(passwordField.getText())
                .setFromAddress(fromAddressField.getText())
                .setBccAddress(bccField.getText())
                .setPort(passwordField.getText());
    }

    private void configureComboBox() {
        transportComboBox.setItems(FXCollections.observableArrayList(TransportType.values()));
        transportComboBox.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(TransportType transportType, boolean b) {
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
