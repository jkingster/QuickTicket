package io.jacobking.quickticket.gui.custom;

import io.jacobking.quickticket.gui.NameModel;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;

public class CustomSearchBox<T extends NameModel<?>> extends SearchableComboBox<T> {

    public CustomSearchBox() {
        setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    return;
                }

                setText(item.getName());
            }
        });

        setConverter(new StringConverter<>() {
            @Override public String toString(T item) {
                return (item == null) ? "Unknown" : item.getName();
            }

            @Override public T fromString(String s) {
                return null;
            }
        });
    }

}
