package com.sinjinsong.webserver.core.servlet.impl;

import com.sinjinsong.webserver.core.enumeration.RequestMethod;
import com.sinjinsong.webserver.core.exception.base.ServletException;
import com.sinjinsong.webserver.core.request.Request;
import com.sinjinsong.webserver.core.response.Response;
import com.sinjinsong.webserver.core.servlet.Servlet;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Created by SinjinSong on 2017/7/21.
 * 根Servlet，实现了不同HTTP方法的路由
 */
@Slf4j
public abstract class HttpServlet implements Servlet {

    @Override
    public void init() {
        
    }

    @Override
    public void destroy() {

    }

    public void service(Request request, Response response) throws ServletException, IOException {
        if (request.getMethod() == RequestMethod.GET) {
            doGet(request, response);
        } else if (request.getMethod() == RequestMethod.POST) {
            doPost(request, response);
        } else if (request.getMethod() == RequestMethod.PUT) {
            doPut(request, response);
        } else if (request.getMethod() == RequestMethod.DELETE) {
            doDelete(request, response);
        }
    }

    public void doGet(Request request, Response response) throws ServletException, IOException {
    }

    public void doPost(Request request, Response response) throws ServletException, IOException {
    }

    public void doPut(Request request, Response response) throws ServletException, IOException {
    }

    public void doDelete(Request request, Response response) throws ServletException, IOException {
    }


}
