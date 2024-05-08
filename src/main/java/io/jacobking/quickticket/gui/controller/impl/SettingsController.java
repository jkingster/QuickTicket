package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.gui.alert.Alerts;
import io.jacobking.quickticket.gui.alert.Notifications;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.model.impl.AlertSettingsModel;
import io.jacobking.quickticket.gui.model.impl.EmailModel;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController extends Controller {

    private AlertSettingsModel alertSettingsModel;

    @FXML private CheckBox disableAlertsCheckBox;
    @FXML private CheckBox disableNotificationsCheckBox;

    @FXML private CheckBox disableInfoAlerts;
    @FXML private CheckBox disableErrorAlerts;
    @FXML private CheckBox disableConfirmationAlerts;
    @FXML private CheckBox disableWarningAlerts;

    @FXML private CheckBox disableInfoNotification;
    @FXML private CheckBox disableErrorNotification;
    @FXML private CheckBox disableConfirmationNotification;
    @FXML private CheckBox disableWarningNotification;

    @FXML private VBox alertsModule;
    @FXML private VBox notificationsModule;

    @FXML private CheckBox emailDebugCheckBox;


    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        this.alertSettingsModel = alertSettings.getModel(0);

        disableAlertsCheckBox.selectedProperty().addListener(((observableValue, aBoolean, t1) -> setAlertValues(t1)));
        alertsModule.disableProperty().bind(disableAlertsCheckBox.selectedProperty());

        disableNotificationsCheckBox.selectedProperty().addListener(((observableValue, aBoolean, t1) -> setNotificationValues(t1)));
        notificationsModule.disableProperty().bind(disableNotificationsCheckBox.selectedProperty());

        bindBidirectional(disableAlertsCheckBox, alertSettingsModel.disableAlertsProperty());
        bindBidirectional(disableNotificationsCheckBox, alertSettingsModel.disableNotificationsProperty());

        bindBidirectional(disableInfoAlerts,         alertSettingsModel.disableInfoAlertsProperty());
        bindBidirectional(disableErrorAlerts,        alertSettingsModel.disableErrorAlertsProperty());
        bindBidirectional(disableConfirmationAlerts, alertSettingsModel.disableConfirmationAlertsProperty());
        bindBidirectional(disableWarningAlerts,      alertSettingsModel.disableWarningAlertsProperty());

        bindBidirectional(disableInfoNotification,         alertSettingsModel.disableInfoNotificationProperty());
        bindBidirectional(disableErrorNotification,        alertSettingsModel.disableErrorNotificationProperty());
        bindBidirectional(disableConfirmationNotification, alertSettingsModel.disableConfirmationNotificationProperty());
        bindBidirectional(disableWarningNotification,      alertSettingsModel.disableWarningNotificationProperty());

        emailDebugCheckBox.setSelected(email.getEmailModel().isDebugging());
        emailDebugCheckBox.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                Alerts.showWarning(
                        "File I/O",
                        "Enabling e-mail debugging has potential File I/O overhead.",
                        "Do not keep this enabled for extended periods, only for testing. Log files will continuously generate.\n\nPlease note- debugging will not be enabled until the QuickTicket client has been restarted."
                );
            }
        });
    }

    private void bindBidirectional(final CheckBox checkBox, final BooleanProperty property) {
        checkBox.selectedProperty().bindBidirectional(property);
    }

    private void setAlertValues(final boolean state) {
        alertSettingsModel.setDisableInfoAlertsProperty(state);
        alertSettingsModel.setDisableErrorAlertsProperty(state);
        alertSettingsModel.setDisableConfirmationAlertsProperty(state);
        alertSettingsModel.setDisableWarningAlertsProperty(state);
    }

    private void setNotificationValues(final boolean state) {
        alertSettingsModel.setDisableInfoNotificationProperty(state);
        alertSettingsModel.setDisableErrorNotificationProperty(state);
        alertSettingsModel.setDisableConfirmationNotificationProperty(state);
        alertSettingsModel.setDisableWarningNotificationProperty(state);
    }

    @FXML private void onUpdate() {
        if (alertSettings.update(alertSettingsModel)) {
            Notifications.showInfo("Update", "Alerts/Notification settings was updated successfully.");
        }
    }

    @FXML private void onUpdateMisc() {
        final EmailModel model = email.getEmailModel();

        model.setDebugging(emailDebugCheckBox.isSelected());
        if (email.update(model)) {
            email.update(model);
            emailConfig.setEmail(model.toEntity());
            Notifications.showInfo("Settings Changed", "Miscellaneous settings changed.");
        }
    }

}
