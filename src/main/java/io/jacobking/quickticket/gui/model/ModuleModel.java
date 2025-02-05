package io.jacobking.quickticket.gui.model;

import io.jacobking.quickticket.gui.Model;
import io.jacobking.quickticket.tables.pojos.Module;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ModuleModel extends Model<Module> {

    private final StringProperty  name        = new SimpleStringProperty();
    private final StringProperty  description = new SimpleStringProperty();
    private final BooleanProperty state       = new SimpleBooleanProperty();

    public ModuleModel(int id, String name, String description, boolean state) {
        super(id);
        this.name.setValue(name);
        this.description.setValue(description);
        this.state.setValue(state);
    }

    public ModuleModel(final Module module) {
        this(
                module.getId(),
                module.getName(),
                module.getDescription(),
                module.getState()
        );
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public boolean isEnabled() {
        return state.get();
    }

    public BooleanProperty stateProperty() {
        return state;
    }

    @Override public Module toEntity() {
        return new Module()
                .setId(getId())
                .setName(getName())
                .setDescription(getDescription())
                .setState(isEnabled());

    }
}
