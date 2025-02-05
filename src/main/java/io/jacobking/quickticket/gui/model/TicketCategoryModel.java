package io.jacobking.quickticket.gui.model;

import io.jacobking.quickticket.gui.Model;
import io.jacobking.quickticket.tables.pojos.TicketCategories;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

public class TicketCategoryModel extends Model<TicketCategories> {

    private final StringProperty nameProperty        = new SimpleStringProperty();
    private final StringProperty colorProperty       = new SimpleStringProperty();
    private final StringProperty descriptionProperty = new SimpleStringProperty();

    public TicketCategoryModel(int id, String name, String color, String description) {
        super(id);
        this.nameProperty.setValue(name);
        this.colorProperty.setValue(color);
        this.descriptionProperty.setValue(description);
    }

    public TicketCategoryModel(final TicketCategories category) {
        this(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getDescription()
        );
    }

    public String getNameProperty() {
        return nameProperty.get();
    }

    public StringProperty namePropertyProperty() {
        return nameProperty;
    }

    public void setNameProperty(String nameProperty) {
        this.nameProperty.set(nameProperty);
    }

    public String getColorProperty() {
        return colorProperty.get();
    }

    public StringProperty colorProperty() {
        return colorProperty;
    }

    public void setColorProperty(String colorProperty) {
        this.colorProperty.set(colorProperty);
    }

    public String getDescriptionProperty() {
        return descriptionProperty.get();
    }

    public StringProperty descriptionProperty() {
        return descriptionProperty;
    }

    public void setDescriptionProperty(String descriptionProperty) {
        this.descriptionProperty.set(descriptionProperty);
    }

    public static String getColorAsRGB(final String colorProperty) {
        final Color color = Color.web(colorProperty);
        final int red = (int) (color.getRed() * 255);
        final int green = (int) (color.getGreen() * 255);
        final int blue = (int) (color.getBlue() * 255);
        return String.format("#%02x%02x%02x", red, green, blue);
    }

    @Override public TicketCategories toEntity() {
        return new TicketCategories()
                .setId(getId())
                .setColor(getColorProperty())
                .setDescription(getDescriptionProperty())
                .setName(getNameProperty());
    }

    @Override public String toString() {
        return getNameProperty();
    }
}
