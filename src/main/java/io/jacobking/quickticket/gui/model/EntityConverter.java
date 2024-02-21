package io.jacobking.quickticket.gui.model;

public interface EntityConverter<E, V extends ViewModel<E>> {
    E convertModel(final V model);
}
