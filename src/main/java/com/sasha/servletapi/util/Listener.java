package com.sasha.servletapi.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Listener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        FlywayMigration.migrate();
    }
}
