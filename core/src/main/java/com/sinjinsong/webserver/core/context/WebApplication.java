package com.sinjinsong.webserver.core.server;

import com.sinjinsong.webserver.core.context.ServletContext;

/**
 * Created by SinjinSong on 2017/7/21.
 */
public class WebApplication {
    private static ServletContext servletContext;
    
    static {
        try {
            servletContext = new ServletContext();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static ServletContext getServletContext() {
        return servletContext;
    }
}
