package com.sinjinsong.webserver.core.listener;

import com.sinjinsong.webserver.core.listener.event.ServletContextEvent;

import java.util.EventListener;

/**
 * @author sinjinsong
 * @date 2018/5/3
 */
public interface ServletContextListener extends EventListener {
    void contextInitialized(ServletContextEvent sce);
    void contextDestroyed(ServletContextEvent sce);
}
