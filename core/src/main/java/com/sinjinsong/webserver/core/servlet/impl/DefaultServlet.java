package com.sinjinsong.webserver.core.servlet.impl;

import com.sinjinsong.webserver.core.enumeration.RequestMethod;
import com.sinjinsong.webserver.core.exception.base.ServletException;
import com.sinjinsong.webserver.core.request.Request;
import com.sinjinsong.webserver.core.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author sinjinsong
 * @date 2018/5/3
 * 如果当前url没有匹配任何的servlet，就会调用默认Servlet，它可以处理静态资源
 */
@Slf4j
public class DefaultServlet extends HttpServlet {

    @Override
    public void service(Request request, Response response) throws ServletException, IOException {
        if (request.getMethod() == RequestMethod.GET) {
            //首页
            if (request.getUrl().equals("/")) {
                request.setUrl("/index.html");
            }
            request.getRequestDispatcher(request.getUrl()).forward(request, response);
        }
    }
}
