package io.jacobking.quickticket.gui.model;

import io.jacobking.quickticket.gui.Model;
import io.jacobking.quickticket.tables.pojos.Alerts;
import javafx.beans.property.*;

public class AlertModel extends Model<Alerts> {

    private final StringProperty  alertName     = new SimpleStringProperty();
    private final BooleanProperty alertState    = new SimpleBooleanProperty();
    private final IntegerProperty alertParentId = new SimpleIntegerProperty();

    public AlertModel(int id, String alertName, boolean alertState, int parentId) {
        super(id);
        this.alertName.setValue(alertName);
        this.alertState.setValue(alertState);
        this.alertParentId.setValue(parentId);
    }

    public AlertModel(final Alerts alerts) {
        this(
                alerts.getAlertId(),
                alerts.getAlertName(),
                alerts.getAlertState(),
                alerts.getAlertParentId()
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

    public boolean isAlertState() {
        return alertState.get();
    }

    public int getAlertParentId() {
        return alertParentId.get();
    }

    public IntegerProperty alertParentIdProperty() {
        return alertParentId;
    }

    public void setAlertParentId(int alertParentId) {
        this.alertParentId.set(alertParentId);
    }

    @Override public Alerts toEntity() {
        return new Alerts()
                .setAlertId(getId())
                .setAlertName(getAlertName())
                .setAlertState(getAlertState())
                .setAlertParentId(getAlertParentId());
    }
}
