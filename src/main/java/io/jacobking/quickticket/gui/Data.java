package io.jacobking.quickticket.gui;

import javafx.beans.property.ObjectProperty;
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

    public <T> Optional<T> mapIndex(final int index, final Class<T> clazz) {
        if (index > objects.length - 1)
            return Optional.empty();

        final T mapped = getMappedObject(objects[index], clazz);
        return mapped == null ? Optional.empty() : Optional.of(mapped);
    }

    public <T> Optional<ObjectProperty<T>> mapObjectProperty(final int index) {
        final int length = objects.length;
        if (index > length - 1 || index < 0)
            return Optional.empty();

        final ObjectProperty<T> mapped = getMappedObject(objects[index], ObjectProperty.class);
        return mapped == null ? Optional.empty() : Optional.of(mapped);
    }

    public <T> Optional<TableView<T>> mapTable(final int index) {
        if (index < 0 || index > objects.length - 1)
            return Optional.empty();

        final TableView<T> mappedObject = getMappedObject(objects[index], TableView.class);
        return Optional.ofNullable(mappedObject);
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
