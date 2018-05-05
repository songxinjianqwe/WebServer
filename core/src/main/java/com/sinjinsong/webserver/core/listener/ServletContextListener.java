package com.sinjinsong.webserver.core.listener;

import com.sinjinsong.webserver.core.listener.event.ServletContextEvent;

import java.util.EventListener;

/**
 * @author sinjinsong
 * @date 2018/5/3
 * 应用层面上的监听器
 */
public interface ServletContextListener extends EventListener {
    /**
     * 应用启动
     * @param sce
     */
    void contextInitialized(ServletContextEvent sce);

    /**
     * 应用关闭
     * @param sce
     */
    void contextDestroyed(ServletContextEvent sce);
}
