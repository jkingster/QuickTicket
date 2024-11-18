package io.jacobking.quickticket.gui.custom;

import io.jacobking.quickticket.gui.NameModel;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;

public class CustomSearchBox<T extends NameModel<?>> extends SearchableComboBox<T> {

    public CustomSearchBox(ObservableList<T> items) {
        super(items);

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

        setConverter(new StringConverter<T>() {
            @Override public String toString(T t) {
                return (t == null) ? "Unknown" : t.getName();
            }

            @Override public T fromString(String s) {
                return null;
            }
        });
    }
}
