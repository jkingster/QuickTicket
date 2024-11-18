package io.jacobking.quickticket.gui.misc;

import io.jacobking.quickticket.App;
import io.jacobking.quickticket.core.utility.Checks;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.controlsfx.control.PopOver;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class PopOverBuilder {

    private final static List<String> STYLESHEETS_EXTERNAL_FORM = new ArrayList<>();

    static {
        for (final String resource : Arrays.asList("checkbox.css", "combo-box.css", "button.css", "text-field.css", "check-list-view.css")) {
            final String converted = String.format("css/core/%s", resource);
            final URL url = App.class.getResource(converted);
            if (url == null) {
                continue;
            }

            final String externalForm = url.toExternalForm();
            STYLESHEETS_EXTERNAL_FORM.add(externalForm);
        }
    }

    private final PopOver popOver;

    private PopOverBuilder() {
        this.popOver = new PopOver();

        popOver.getRoot()
                .getStylesheets()
                .addAll(STYLESHEETS_EXTERNAL_FORM);
    }

    public static PopOverBuilder build() {
        return new PopOverBuilder();
    }

    public PopOverBuilder setTitle(final String title) {
        this.popOver.setTitle(title);
        return this;
    }

    public PopOverBuilder setHeight(final double height) {
        this.popOver.setMinHeight(0);
        this.popOver.setMaxHeight(0);
        this.popOver.setPrefHeight(0);
        return this;
    }

    public PopOverBuilder setContent(final Node content) {
        this.popOver.setContentNode(content);
        return this;
    }

    public PopOverBuilder setAnimated(final boolean animated) {
        this.popOver.setAnimated(animated);
        return this;
    }

    public PopOverBuilder setArrowLocation(final PopOver.ArrowLocation arrowLocation) {
        this.popOver.setArrowLocation(arrowLocation);
        return this;
    }

    public PopOverBuilder setDetachable(final boolean detachable) {
        this.popOver.setDetachable(detachable);
        return this;
    }

    public PopOverBuilder setDetached(final boolean detached) {
        this.popOver.setDetached(detached);
        return this;
    }

    public PopOverBuilder process(final Function<Void, Node> process) {
        final Node processedNode = process.apply(null);
        return setContent(processedNode);
    }

    public PopOverBuilder setHideOnEscape(final boolean state) {
        this.popOver.setHideOnEscape(state);
        return this;
    }

    public void show(final Node owner, final int offset) {
        this.popOver.show(owner, offset);
    }

}
