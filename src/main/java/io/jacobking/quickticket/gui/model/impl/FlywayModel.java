package io.jacobking.quickticket.gui.model.impl;

import io.jacobking.quickticket.gui.model.Model;
import io.jacobking.quickticket.tables.pojos.FlywaySchemaHistory;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FlywayModel extends Model<FlywaySchemaHistory> {

    private final IntegerProperty installedRank = new SimpleIntegerProperty();
    private final StringProperty  version       = new SimpleStringProperty();
    private final StringProperty  description   = new SimpleStringProperty();
    private final StringProperty  type          = new SimpleStringProperty();
    private final StringProperty  script        = new SimpleStringProperty();
    private final IntegerProperty checksum      = new SimpleIntegerProperty();
    private final StringProperty  installedOn   = new SimpleStringProperty();

    public FlywayModel(int installedRank, String version, String description, String type, String script, int checksum, String installedOn) {
        super(installedRank);
        this.version.setValue(version);
        this.description.setValue(description);
        this.type.setValue(type);
        this.script.setValue(script);
        this.checksum.setValue(checksum);
        this.installedOn.setValue(installedOn);
    }

    public FlywayModel(final FlywaySchemaHistory flyway) {
        this(
                flyway.getInstalledRank(),
                flyway.getVersion(),
                flyway.getDescription(),
                flyway.getType(),
                flyway.getScript(),
                flyway.getChecksum() == null ? -1 : flyway.getChecksum(),
                flyway.getInstalledOn()
        );
    }

    public int getInstalledRank() {
        return installedRank.get();
    }

    public IntegerProperty installedRankProperty() {
        return installedRank;
    }

    public void setInstalledRank(int installedRank) {
        this.installedRank.set(installedRank);
    }

    public String getVersion() {
        return version.get();
    }

    public StringProperty versionProperty() {
        return version;
    }

    public void setVersion(String version) {
        this.version.set(version);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getScript() {
        return script.get();
    }

    public StringProperty scriptProperty() {
        return script;
    }

    public void setScript(String script) {
        this.script.set(script);
    }

    public int getChecksum() {
        return checksum.get();
    }

    public IntegerProperty checksumProperty() {
        return checksum;
    }

    public void setChecksum(int checksum) {
        this.checksum.set(checksum);
    }

    public String getInstalledOn() {
        return installedOn.get();
    }

    public StringProperty installedOnProperty() {
        return installedOn;
    }

    public void setInstalledOn(String installedOn) {
        this.installedOn.set(installedOn);
    }


    @Override public FlywaySchemaHistory toEntity() {
        return new FlywaySchemaHistory()
                .setInstalledRank(getInstalledRank())
                .setVersion(getVersion())
                .setDescription(getDescription())
                .setType(getType())
                .setChecksum(getChecksum())
                .setInstalledOn(getInstalledOn());
    }
}
