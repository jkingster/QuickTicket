package io.jacobking.quickticket.core.config;


import io.jacobking.quickticket.core.utility.FileIO;

public class SystemConfig extends Config {

    public SystemConfig() {
        super("system.properties");
    }


    @Override public void putDefaultProperties() {
        setProperty("database_url", FileIO.getPath("database.db"));
        setProperty("auto_migration", "true");
        setProperty("first_launch", "true");
    }
}
