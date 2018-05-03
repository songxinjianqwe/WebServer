package com.sinjinsong.webserver.core.servlet;

import com.sinjinsong.webserver.core.exception.base.ServletException;
import com.sinjinsong.webserver.core.request.Request;
import com.sinjinsong.webserver.core.response.Response;

import java.io.IOException;

/**
 * @author sinjinsong
 * @date 2018/5/2
 */
public interface Servlet {
    void init();

    void destroy();

    void service(Request request, Response response) throws ServletException, IOException;
}
