package io.jacobking.quickticket.gui.model.impl;

import io.jacobking.quickticket.gui.model.ViewModel;
import io.jacobking.quickticket.tables.pojos.Inventory;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InventoryModel extends ViewModel<Inventory> {

    private final StringProperty  assetName      = new SimpleStringProperty();
    private final IntegerProperty totalCount     = new SimpleIntegerProperty();
    private final IntegerProperty lastIssued     = new SimpleIntegerProperty();
    private final StringProperty  lastIssuedDate = new SimpleStringProperty();

    public InventoryModel(final int id, String assetName, int totalCount, int issued, String lastIssuedDate) {
        super(id);
        this.assetName.setValue(assetName);
        this.totalCount.setValue(totalCount);
        this.lastIssued.setValue(issued);
        this.lastIssuedDate.setValue(lastIssuedDate);
    }

    public InventoryModel(final Inventory inventory) {
        this(
                inventory.getId(),
                inventory.getAssetName(),
                inventory.getTotalCount(),
                inventory.getLastIssued() == null ? -1 : inventory.getLastIssued(),
                inventory.getLastIssuedDate()
        );
    }

    public String getAssetName() {
        return assetName.get();
    }

    public StringProperty assetNameProperty() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName.set(assetName);
    }

    public int getTotalCount() {
        return totalCount.get();
    }

    public IntegerProperty totalCountProperty() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount.set(totalCount);
    }

    public int getLastIssued() {
        return lastIssued.get();
    }

    public IntegerProperty lastIssuedProperty() {
        return lastIssued;
    }

    public void setLastIssued(int lastIssued) {
        this.lastIssued.set(lastIssued);
    }

    public String getLastIssuedDate() {
        return lastIssuedDate.get();
    }

    public StringProperty lastIssuedDateProperty() {
        return lastIssuedDate;
    }

    public void setLastIssuedDate(String lastIssuedDate) {
        this.lastIssuedDate.set(lastIssuedDate);
    }

    @Override public Inventory toEntity() {
        return new Inventory()
                .setId(getId())
                .setAssetName(getAssetName())
                .setTotalCount(getTotalCount())
                .setLastIssued(getLastIssued())
                .setLastIssuedDate(getLastIssuedDate());
    }
}
