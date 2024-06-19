package io.jacobking.quickticket.gui.misc;

import io.jacobking.quickticket.App;
import io.jacobking.quickticket.core.utility.Checks;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.controlsfx.control.PopOver;

import java.net.URL;

public class PopOverBuilder {

    private static final int DEFAULT_OFFSET = 10;

    private final PopOver popOver;

    private Parent owner;

    public PopOverBuilder() {
        this.popOver = new PopOver();
        initializeStyle();
    }

    public void show() {
        if (owner == null) {
            throw new UnsupportedOperationException("PopOver owner is not set!");
        }
        popOver.show(owner, DEFAULT_OFFSET);
    }

    public PopOverBuilder useDefaultSettings() {
        popOver.setDetachable(false);
        popOver.setAnimated(true);
        popOver.setHeaderAlwaysVisible(true);
        return this;
    }

    public PopOverBuilder setOwner(final Parent parent) {
        this.owner = parent;
        return this;
    }

    public PopOverBuilder setTitle(final String title) {
        Checks.notEmpty(title, "PopOver Title");
        popOver.setTitle(title);
        return this;
    }

    public PopOverBuilder setArrowOrientation(final PopOver.ArrowLocation arrowOrientation) {
        Checks.notNull(arrowOrientation, "Arrow Orientation");
        popOver.setArrowLocation(arrowOrientation);
        return this;
    }

    public PopOver get() {
        return popOver;
    }

    public PopOverBuilder setContent(final Node node) {
        Checks.notNull(node, "PopOver Content Node");
        popOver.setContentNode(node);
        return this;
    }

    public void hide() {
        popOver.hide();
    }

    private void initializeStyle() {
        final URL stylesheet = App.class.getResource("css/core/pop-over.css");
        if (stylesheet == null)
            return;

        final String externalForm = stylesheet.toExternalForm();
        popOver.getRoot().getStylesheets().add(externalForm);
    }
}
