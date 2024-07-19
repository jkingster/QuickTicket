package io.jacobking.quickticket.gui.model.impl;

import io.jacobking.quickticket.gui.model.ViewModel;
import io.jacobking.quickticket.tables.pojos.Alerts;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AlertModel extends ViewModel<Alerts> {

    private final StringProperty  alertName  = new SimpleStringProperty();
    private final BooleanProperty alertState = new SimpleBooleanProperty();

    public AlertModel(int id, String alertName, boolean alertState) {
        super(id);
        this.alertName.setValue(alertName);
        this.alertState.setValue(alertState);
    }

    public AlertModel(final Alerts alerts) {
        this(
                alerts.getAlertId(),
                alerts.getAlertName(),
                alerts.getAlertState()
        );
    }

    public String getAlertName() {
        return alertName.get();
    }

    public StringProperty alertNameProperty() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName.set(alertName);
    }

    public boolean getAlertState() {
        return alertState.get();
    }

    public BooleanProperty alertStateProperty() {
        return alertState;
    }

    public void setAlertState(boolean alertState) {
        this.alertState.set(alertState);
    }

    @Override public Alerts toEntity() {
        return new Alerts()
                .setAlertId(getId())
                .setAlertName(getAlertName())
                .setAlertState(getAlertState());
    }
}
