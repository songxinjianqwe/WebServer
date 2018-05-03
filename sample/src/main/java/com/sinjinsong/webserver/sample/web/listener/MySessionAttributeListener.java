package com.sinjinsong.webserver.sample.web.listener;

import com.sinjinsong.webserver.core.listener.HttpSessionAttributeListener;
import com.sinjinsong.webserver.core.listener.event.HttpSessionBindingEvent;

/**
 * @author sinjinsong
 * @date 2018/5/3
 */
public class MySessionAttributeListener implements HttpSessionAttributeListener {
    
    
    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {

    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {

    }
}
