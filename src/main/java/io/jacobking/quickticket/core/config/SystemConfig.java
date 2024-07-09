package io.jacobking.quickticket.core.config;


import io.jacobking.quickticket.core.utility.FileIO;

public class SystemConfig extends Config {

    public SystemConfig() {
        super("system.properties");
    }

    @Override public void setDefaultProperties() {
        putDefaultProperty("database_url", FileIO.getPath("database.db"));
        putDefaultProperty("auto_migration", "true");
        putDefaultProperty("first_launch", "true");
        putDefaultProperty("disable_file_locking", "false");
        putDefaultProperty("testing", "true");
    }
}
