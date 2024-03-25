package io.jacobking.quickticket.gui.model.impl;

import io.jacobking.quickticket.gui.model.ViewModel;
import io.jacobking.quickticket.tables.pojos.AlertSettings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class AlertSettingsModel extends ViewModel<AlertSettings> {

    private final BooleanProperty disableAlertsProperty                   = new SimpleBooleanProperty();
    private final BooleanProperty disableNotificationsProperty            = new SimpleBooleanProperty();
    private final BooleanProperty disableInfoAlertsProperty               = new SimpleBooleanProperty();
    private final BooleanProperty disableErrorAlertsProperty              = new SimpleBooleanProperty();
    private final BooleanProperty disableConfirmationAlertsProperty       = new SimpleBooleanProperty();
    private final BooleanProperty disableWarningAlertsProperty            = new SimpleBooleanProperty();
    private final BooleanProperty disableInfoNotificationProperty         = new SimpleBooleanProperty();
    private final BooleanProperty disableErrorNotificationProperty        = new SimpleBooleanProperty();
    private final BooleanProperty disableConfirmationNotificationProperty = new SimpleBooleanProperty();
    private final BooleanProperty disableWarningNotificationProperty      = new SimpleBooleanProperty();


    public AlertSettingsModel(int id, boolean disableAlerts, boolean disableNotifications, boolean disableInfoAlerts, boolean disableErrorAlerts, boolean disableConfirmationAlerts, boolean disableWarningAlerts, boolean disableInfoNotification, boolean disableErrorNotification, boolean disableConfirmationNotification, boolean disableWarningNotification) {
        super(id);
        this.disableAlertsProperty.setValue(disableAlerts);
        this.disableNotificationsProperty.setValue(disableNotifications);
        this.disableInfoAlertsProperty.setValue(disableInfoAlerts);
        this.disableErrorAlertsProperty.setValue(disableErrorAlerts);
        this.disableConfirmationAlertsProperty.setValue(disableConfirmationAlerts);
        this.disableWarningAlertsProperty.setValue(disableWarningAlerts);
        this.disableInfoNotificationProperty.setValue(disableInfoNotification);
        this.disableErrorNotificationProperty.setValue(disableErrorNotification);
        this.disableConfirmationNotificationProperty.setValue(disableConfirmationNotification);
        this.disableWarningNotificationProperty.setValue(disableWarningNotification);
    }

    public AlertSettingsModel(final AlertSettings alertSettings) {
        this(
                alertSettings.getId(),
                alertSettings.getDisableAlerts(),
                alertSettings.getDisableNotifications(),
                alertSettings.getDisableInfoAlerts(),
                alertSettings.getDisableErrorAlerts(),
                alertSettings.getDisableConfirmationAlerts(),
                alertSettings.getDisableWarningAlerts(),
                alertSettings.getDisableInfoNotification(),
                alertSettings.getDisableErrorNotification(),
                alertSettings.getDisableConfirmationNotification(),
                alertSettings.getDisableWarningNotification()
        );
    }

    public boolean isDisableAlertsProperty() {
        return disableAlertsProperty.get();
    }

    public BooleanProperty disableAlertsProperty() {
        return disableAlertsProperty;
    }

    public void setDisableAlertsProperty(boolean disableAlertsProperty) {
        this.disableAlertsProperty.set(disableAlertsProperty);
    }

    public boolean isDisableNotificationsProperty() {
        return disableNotificationsProperty.get();
    }

    public BooleanProperty disableNotificationsProperty() {
        return disableNotificationsProperty;
    }

    public void setDisableNotificationsProperty(boolean disableNotificationsProperty) {
        this.disableNotificationsProperty.set(disableNotificationsProperty);
    }

    public boolean isDisableInfoAlertsProperty() {
        return disableInfoAlertsProperty.get();
    }

    public BooleanProperty disableInfoAlertsProperty() {
        return disableInfoAlertsProperty;
    }

    public void setDisableInfoAlertsProperty(boolean disableInfoAlertsProperty) {
        this.disableInfoAlertsProperty.set(disableInfoAlertsProperty);
    }

    public boolean isDisableErrorAlertsProperty() {
        return disableErrorAlertsProperty.get();
    }

    public BooleanProperty disableErrorAlertsProperty() {
        return disableErrorAlertsProperty;
    }

    public void setDisableErrorAlertsProperty(boolean disableErrorAlertsProperty) {
        this.disableErrorAlertsProperty.set(disableErrorAlertsProperty);
    }

    public boolean isDisableConfirmationAlertsProperty() {
        return disableConfirmationAlertsProperty.get();
    }

    public BooleanProperty disableConfirmationAlertsProperty() {
        return disableConfirmationAlertsProperty;
    }

    public void setDisableConfirmationAlertsProperty(boolean disableConfirmationAlertsProperty) {
        this.disableConfirmationAlertsProperty.set(disableConfirmationAlertsProperty);
    }

    public boolean isDisableWarningAlertsProperty() {
        return disableWarningAlertsProperty.get();
    }

    public BooleanProperty disableWarningAlertsProperty() {
        return disableWarningAlertsProperty;
    }

    public void setDisableWarningAlertsProperty(boolean disableWarningAlertsProperty) {
        this.disableWarningAlertsProperty.set(disableWarningAlertsProperty);
    }

    public boolean isDisableInfoNotificationProperty() {
        return disableInfoNotificationProperty.get();
    }

    public BooleanProperty disableInfoNotificationProperty() {
        return disableInfoNotificationProperty;
    }

    public void setDisableInfoNotificationProperty(boolean disableInfoNotificationProperty) {
        this.disableInfoNotificationProperty.set(disableInfoNotificationProperty);
    }

    public boolean isDisableErrorNotificationProperty() {
        return disableErrorNotificationProperty.get();
    }

    public BooleanProperty disableErrorNotificationProperty() {
        return disableErrorNotificationProperty;
    }

    public void setDisableErrorNotificationProperty(boolean disableErrorNotificationProperty) {
        this.disableErrorNotificationProperty.set(disableErrorNotificationProperty);
    }

    public boolean isDisableConfirmationNotificationProperty() {
        return disableConfirmationNotificationProperty.get();
    }

    public BooleanProperty disableConfirmationNotificationProperty() {
        return disableConfirmationNotificationProperty;
    }

    public void setDisableConfirmationNotificationProperty(boolean disableConfirmationNotificationProperty) {
        this.disableConfirmationNotificationProperty.set(disableConfirmationNotificationProperty);
    }

    public boolean isDisableWarningNotificationProperty() {
        return disableWarningNotificationProperty.get();
    }

    public BooleanProperty disableWarningNotificationProperty() {
        return disableWarningNotificationProperty;
    }

    public void setDisableWarningNotificationProperty(boolean disableWarningNotificationProperty) {
        this.disableWarningNotificationProperty.set(disableWarningNotificationProperty);
    }

    @Override public AlertSettings toEntity() {
        return new AlertSettings()
                .setId(getId())
                .setDisableAlerts(isDisableAlertsProperty())
                .setDisableNotifications(isDisableNotificationsProperty())
                .setDisableConfirmationAlerts(isDisableConfirmationAlertsProperty())
                .setDisableInfoAlerts(isDisableInfoAlertsProperty())
                .setDisableErrorAlerts(isDisableErrorAlertsProperty())
                .setDisableWarningAlerts(isDisableWarningAlertsProperty())
                .setDisableInfoNotification(isDisableInfoNotificationProperty())
                .setDisableErrorNotification(isDisableErrorNotificationProperty())
                .setDisableConfirmationNotification(isDisableConfirmationNotificationProperty())
                .setDisableWarningNotification(isDisableWarningNotificationProperty());
    }
}
