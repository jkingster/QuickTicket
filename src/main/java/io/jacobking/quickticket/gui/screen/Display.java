package io.jacobking.quickticket.gui.screen;


import javafx.stage.Stage;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class Display {
    private static final String SCREEN_PACKAGE = "io.jacobking.quickticket.gui.screen.impl.%sScreen";
    private final Map<Route, Screen> screens = new HashMap<>();

    public Display() {
        loadScreens();
    }

    public void show(final Route route) {
        final Screen screen = screens.get(route);
        if (screen == null) {
            return;
        }
        screen.show();
    }

    public void close(final Route route) {
        final Screen screen = screens.get(route);
        if (screen == null)
            return;
        screen.close();
    }

    private void loadScreens() {
        for (final Route route : Route.getValues()) {
            loadScreen(route);
        }
    }

    private void loadScreen(final Route route) {
        screens.computeIfAbsent(route, (target) -> {
            final String className = SCREEN_PACKAGE.formatted(target.getName());
            final Class<?> clazz = loadClass(className);
            final Constructor<?> constructor = loadConstructor(clazz);
            final Screen screen = newScreenInstance(constructor);
            screen.setStage(new Stage());
            return screen;
        });
    }

    private Class<?> loadClass(final String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Constructor<?> loadConstructor(final Class<?> clazz) {
        try {
            return clazz.getConstructor(Display.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Screen newScreenInstance(final Constructor<?> constructor) {
        try {
            return (Screen) constructor.newInstance(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
