package cn.sinjinsong.server;

import cn.sinjinsong.server.servlet.context.ServletContext;

/**
 * Created by SinjinSong on 2017/7/21.
 */
public class WebApplication {
    private static ServletContext servletContext;

    static {
        servletContext = new ServletContext();
    }

    public static ServletContext getServletContext() {
        return servletContext;
    }

}
