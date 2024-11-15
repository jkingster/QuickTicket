package io.jacobking.quickticket.bridge;

import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoCrud;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.Model;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.function.Predicate;

public abstract class Bridge<E, V extends Model<E>> {
    private final   ObservableList<V> observableList;
    private final   RepoType          repoType;
    private final   BridgeContext     bridgeContext;
    protected final RepoCrud          crud;


    public Bridge(final Database database, final RepoType repoType, final BridgeContext bridgeContext) {
        this.crud = database.call();
        this.observableList = FXCollections.observableArrayList();
        this.repoType = repoType;
        this.bridgeContext = bridgeContext;
        loadEntities();
        removalListener();
    }

    public Bridge(final Database database, final RepoType repoType) {
        this.crud = database.call();
        this.observableList = FXCollections.observableArrayList();
        this.repoType = repoType;
        this.bridgeContext = null;
        loadEntities();
        removalListener();
    }


    protected void loadEntities() {
        final List<E> entities = crud.getAll(repoType);
        if (entities.isEmpty())
            return;

        for (final E entity : entities) {
            final V converted = convertEntity(entity);
            observableList.add(converted);
        }
    }

    public abstract V convertEntity(final E entity);

    public E fetch(final int id) {
        return crud.getById(repoType, id);
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
        Platform.runLater(() -> observableList.add(0, model));
    }

    public V createModel(final E entity) {
        final E returnedEntity = crud.save(repoType, entity);
        final V model = convertEntity(returnedEntity);

        if (model != null) {
            addModel(model);
        }

        return model;
    }

    public boolean update(final V model) {
        return crud.update(repoType, model.toEntity());
    }

    public boolean remove(final int id) {
        if (!contains(id))
            return false;

        return observableList.removeIf(__ -> __.getId() == id);
        //  Platform.runLater(() -> observableList.removeIf(predicate -> predicate.getId() == id));
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

    public ObservableList<V> getObservableListByFilter(final Predicate<V> filter) {
        return getObservableList().filtered(filter);
    }

    public V getFirst() {
        return getObservableList().get(0);
    }

    public List<E> getOriginalEntities() {
        return crud.getAll(repoType);
    }

    public BridgeContext getBridgeContext() {
        return bridgeContext;
    }

    public void removalListener() {
        observableList.addListener((ListChangeListener<? super V>) change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(removed -> {
                        final int id = removed.getId();
                        if (!crud.deleteWhere(repoType, id)) {
                            // TODO: Alert?
                        }
                    });
                }
            }
        });
    }
}