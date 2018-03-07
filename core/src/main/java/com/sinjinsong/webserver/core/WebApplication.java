package com.sinjinsong.webserver.core;

import com.sinjinsong.webserver.core.servlet.context.ServletContext;

/**
 * Created by SinjinSong on 2017/7/21.
 */
public class WebApplication {
    private static ServletContext servletContext;

    public static void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        servletContext = new ServletContext();

    }

    public static ServletContext getServletContext() {
        return servletContext;
    }
}
