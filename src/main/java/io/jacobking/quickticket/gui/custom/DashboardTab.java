package io.jacobking.quickticket.gui.custom;

import io.jacobking.quickticket.gui.utility.IconLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

public class DashboardTab {

    private static final String DASH_TAB_ACTIVE   = "tab-labels-active";
    private static final String DASH_TAB_INACTIVE = "tab-labels";

    private final String     title;
    private final Label      icon;
    private final Label      text;
    private final AnchorPane content;

    public DashboardTab(String title, HBox tab, Label icon, Label text, final AnchorPane content) {
        this.title = title;
        this.icon = icon;
        this.text = text;
        this.content = content;
    }

    public void activate() {
        text.getStyleClass().add(DASH_TAB_ACTIVE);
        text.getStyleClass().add(DASH_TAB_INACTIVE);
        content.setVisible(true);
        content.setDisable(false);
    }

    public void deactivate() {
        text.getStyleClass().add(DASH_TAB_INACTIVE);
        text.getStyleClass().remove(DASH_TAB_ACTIVE);
        content.setVisible(false);
        content.setDisable(true);
    }

    public void setIcon(final Ikon ikon) {
        icon.setGraphic(IconLoader.getMaterialIcon(ikon));
    }

    public void setIcon(final FontIcon fontIcon) {
        icon.setGraphic(IconLoader.getMaterialIcon(fontIcon));
    }

    public String getTitle() {
        return title;
    }

}
