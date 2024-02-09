package io.jacobking.quickticket.gui.data;

import java.util.Arrays;
import java.util.Optional;

public class DataRelay {

    private final Object[] objects;

    private DataRelay(final Object... objects) {
        this.objects = Arrays.copyOf(objects, objects.length);
    }

    public static DataRelay of(final Object object, final Object... objects) {
        if (objects.length == 0)
            return new DataRelay(object);
        return new DataRelay(objects);
    }

    public <T> Optional<T> mapFirstInto(final Class<T> clazz) {
        if (objects.length == 0)
            return Optional.empty();
        final T mapped = getMappedObject(objects[0], clazz);
        return mapped == null ? Optional.empty() : Optional.of(mapped);
    }

    public <T> Optional<T> mapAtIndex(final int index, final Class<T> clazz) {
        final int length = objects.length;
        if (index > length - 1 || index < 0)
            return Optional.empty();

        final T mapped = getMappedObject(objects[index], clazz);
        return mapped == null ? Optional.empty() : Optional.of(mapped);
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
