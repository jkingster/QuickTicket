package io.jacobking.quickticket.gui.misc;

import io.jacobking.quickticket.core.utility.Checks;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.controlsfx.control.PopOver;

import java.util.function.Consumer;

public class PopOverBuilder {

    private static final int    DEFAULT_OFFSET = 10;
    private static final String DEFAULT_STYLE  = "-fx-background-color: #282B36;";

    private final PopOver popOver;

    private Parent owner;

    private PopOverBuilder() {
        this.popOver = new PopOver();
    }

    public static PopOverBuilder build() {
        return new PopOverBuilder();
    }

    public PopOverBuilder useDefault() {
        popOver.setDetachable(false);
        popOver.setAnimated(true);
        popOver.setHeaderAlwaysVisible(true);
        popOver.setStyle(DEFAULT_STYLE);
        return this;
    }

    public PopOverBuilder setOwner(final Parent parent) {
        this.owner = parent;
        return this;
    }

    public PopOverBuilder withTitle(final String title) {
        Checks.notEmpty(title, "PopOver Title");
        popOver.setTitle(title);
        return this;
    }

    public PopOverBuilder withContent(final Node node) {
        Checks.notNull(node, "PopOver Content Node");
        popOver.setContentNode(node);
        return this;
    }

    public PopOverBuilder processPopOver(final Consumer<PopOver> popOverConsumer) {
        popOverConsumer.accept(popOver);
        return this;
    }

    public void show() {
        popOver.show(owner, DEFAULT_OFFSET);
    }

    public void showWithoutOffset() {
        popOver.show(owner);
    }

    public void hide() {
        popOver.hide();
    }

    public PopOver getPopOver() {
        return popOver;
    }
}
