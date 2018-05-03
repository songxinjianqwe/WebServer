package com.sinjinsong.webserver.sample.web.listener;

import com.sinjinsong.webserver.core.listener.HttpSessionListener;
import com.sinjinsong.webserver.core.listener.ServletContextListener;
import com.sinjinsong.webserver.core.listener.event.HttpSessionEvent;
import com.sinjinsong.webserver.core.listener.event.ServletContextEvent;
import lombok.extern.slf4j.Slf4j;


/**
 * @author sinjinsong
 * @date 2017/12/24
 */
@Slf4j
public class ServletContextAndSessionListener implements ServletContextListener,HttpSessionListener {
    private int sessionCount = 0;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("servlet context init...");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("servlet context destroy...");
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        this.sessionCount++;
        log.info("session created, count = {}",this.sessionCount);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        this.sessionCount--;
        log.info("session destroyed, count = {}",this.sessionCount);
    }
}
