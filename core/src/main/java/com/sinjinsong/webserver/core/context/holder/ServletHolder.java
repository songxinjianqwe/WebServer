package com.sinjinsong.webserver.core.context.holder;

import com.sinjinsong.webserver.core.servlet.Servlet;
import lombok.Data;

/**
 * @author sinjinsong
 * @date 2018/5/2
 */
@Data
public class ServletHolder {
    private Servlet servlet;
    private String servletClass;

    public ServletHolder(String servletClass) {
        this.servletClass = servletClass;
    }
}
