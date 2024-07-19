package io.jacobking.quickticket.gui.screen;


import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.data.DataRelay;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class Display {
    private static Display display;

    private static final String             SCREEN_PACKAGE = "io.jacobking.quickticket.gui.screen.impl.%sScreen";
    private final        Map<Route, Screen> screens        = new HashMap<>();

    private Display() {
        loadScreens();
    }

    public static synchronized  Display getInstance() {
        if (display == null) {
            display = new Display();
        }
        return display;
    }

    public static void show(final Route route, final DataRelay dataRelay) {
        getInstance().showRoute(route, dataRelay);
    }

    public static void show(final Route route) {
        show(route, DataRelay.empty());
    }

    public static void close(final Route route) {
        getInstance().closeRoute(route);
    }

    public static void closeAll() {
        getInstance().closeAllRoutes();
    }

    public void showRoute(final Route route, final DataRelay dataRelay) {
        final Screen screen = screens.get(route);
        if (screen == null) {
            final RuntimeException exception = new RuntimeException("Failed to load screen: " + route.getName());
            Announcements.get().showException("Failed to load screen.", exception.fillInStackTrace());
            return;
        }
        screen.display(dataRelay);
    }

    public void closeRoute(final Route route) {
        final Screen screen = screens.get(route);
        if (screen == null)
            return;
        screen.close();
    }

    public void closeAllRoutes() {
        screens.values().forEach(Screen::close);
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
            return newScreenInstance(constructor);
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
            return clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Screen newScreenInstance(final Constructor<?> constructor) {
        try {
            return (Screen) constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
