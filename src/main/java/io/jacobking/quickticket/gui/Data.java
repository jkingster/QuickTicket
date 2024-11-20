package io.jacobking.quickticket.gui;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

import java.util.Arrays;
import java.util.Optional;

@SuppressWarnings({"unchecked"})
public class Data {

    private final Object[] objects;

    private Data(final Object... objects) {
        this.objects = Arrays.copyOf(objects, objects.length);
    }

    public static Data of(final Object... objects) {
        return new Data(objects);
    }

    public static Data empty() {
        return new Data();
    }

    public <T> Optional<T> mapFirst(final Class<T> clazz) {
        if (objects.length == 0)
            return Optional.empty();
        final T mapped = getMappedObject(objects[0], clazz);
        return mapped == null ? Optional.empty() : Optional.of(mapped);
    }


    public <T> T mapIndex(final int index, final Class<T> clazz) {
        if (index > objects.length - 1)
            return null;
        return getMappedObject(objects[index], clazz);
    }

    public <T> TableView<T> mapTable(final int index) {
        if (index > objects.length - 1)
            return null;
        return getMappedObject(objects[index], TableView.class);
    }

    public <T> ListView<T> mapList(final int index) {
        if (index > objects.length - 1)
            return null;
        return getMappedObject(objects[index], ListView.class);
    }

    private <T> T getMappedObject(final Object object, final Class<T> clazz) {
        if (clazz.isInstance(object))
            return clazz.cast(object);
        return null;
    }

    @Override
    public String toString() {
        return "DataRelay{" +
                "objects=" + Arrays.toString(objects) +
                '}';
    }
}
