package io.jacobking.quickticket.gui.model.impl;

import io.jacobking.quickticket.gui.model.Model;
import io.jacobking.quickticket.tables.pojos.InventoryLog;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InventoryLogModel extends Model<InventoryLog> {

    private final IntegerProperty assetId     = new SimpleIntegerProperty();
    private final IntegerProperty countAtTime = new SimpleIntegerProperty();
    private final IntegerProperty employeeId  = new SimpleIntegerProperty();
    private final StringProperty  issuedDate  = new SimpleStringProperty();

    public InventoryLogModel(int id, int assetId, int countAtTime, int employeeId, String issuedDate) {
        super(id);
        this.assetId.setValue(assetId);
        this.countAtTime.setValue(countAtTime);
        this.employeeId.setValue(employeeId);
        this.issuedDate.setValue(issuedDate);
    }

    public InventoryLogModel(final InventoryLog inventoryLog) {
        this(
                inventoryLog.getId(),
                inventoryLog.getAssetId(),
                inventoryLog.getCountAtTime(),
                inventoryLog.getEmployeeId(),
                inventoryLog.getIssuedDate()
        );
    }

    public int getAssetId() {
        return assetId.get();
    }

    public IntegerProperty assetIdProperty() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId.set(assetId);
    }

    public int getCountAtTime() {
        return countAtTime.get();
    }

    public IntegerProperty countAtTimeProperty() {
        return countAtTime;
    }

    public void setCountAtTime(int countAtTime) {
        this.countAtTime.set(countAtTime);
    }

    public int getEmployeeId() {
        return employeeId.get();
    }

    public IntegerProperty employeeIdProperty() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId.set(employeeId);
    }

    public String getIssuedDate() {
        return issuedDate.get();
    }

    public StringProperty issuedDateProperty() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate.set(issuedDate);
    }

    @Override public InventoryLog toEntity() {
        return new InventoryLog()
                .setId(getId())
                .setAssetId(getAssetId())
                .setEmployeeId(getEmployeeId())
                .setIssuedDate(getIssuedDate())
                .setCountAtTime(getCountAtTime());
    }
}
