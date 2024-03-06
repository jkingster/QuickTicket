package io.jacobking.quickticket.bridge;

import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.Entity;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.ViewModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.List;

public abstract class Bridge<E extends Entity, V extends ViewModel<E>> {
    private final ObservableList<V> observableList;
    private final RepoType          repoType;

    public Bridge(final RepoType repoType) {
        this.observableList = FXCollections.observableArrayList();
        this.repoType = repoType;
        loadEntities();
        removalListener();
    }

    protected void loadEntities() {
        final List<E> entities = Database.call().getAll(repoType);
        if (entities.isEmpty())
            return;

        for (final E entity : entities) {
            final V converted = convertEntity(entity);
            observableList.add(converted);
        }
    }

    public abstract V convertEntity(final E entity);

    public E fetch(final int id) {
        return Database.call().getById(repoType, id);
    }

    public V getModel(final int id) {
        if (observableList.isEmpty())
            return null;

        for (final V model : observableList) {
            final int targetId = model.getId();
            if (targetId == id)
                return model;
        }

        final E fetched = fetch(id);
        if (fetched == null)
            return null;

        final V converted = convertEntity(fetched);
        observableList.add(converted);
        return converted;
    }

    public void addModel(final V model) {
        Platform.runLater(() -> {
            observableList.add(model);
        });
    }

    public V createModel(final E entity) {
        final E returnedEntity = Database.call().save(repoType, entity);
        final V model = convertEntity(returnedEntity);

        if (model != null) {
            addModel(model);
        }

        return model;
    }

    public void update(final V model) {
        Database.call().update(repoType, model.toEntity());
    }

    public void remove(final int id) {
        if (!contains(id))
            return;

        Platform.runLater(() -> {
            observableList.removeIf(filter -> filter.getId() == id);
        });
    }

    public boolean contains(final int id) {
        for (final V view : observableList) {
            final int targetId = view.getId();
            if (targetId == id)
                return true;
        }
        return false;
    }

    public ObservableList<V> getObservableList() {
        return observableList;
    }

    private void removalListener() {
        observableList.addListener((ListChangeListener<? super V>) change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(removed -> {
                        final int id = removed.getId();
                        if (!Database.call().delete(repoType, id)) {
                            // TODO: Alert?
                        }
                    });
                }
            }
        });
    }
}