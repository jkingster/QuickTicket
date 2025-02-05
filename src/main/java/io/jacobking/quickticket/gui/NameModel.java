package io.jacobking.quickticket.gui;

public abstract class NameModel<T> extends Model<T> {

    public NameModel(int id) {
        super(id);
    }

    public abstract String getName();
}
