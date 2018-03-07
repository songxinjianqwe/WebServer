package com.sinjinsong.webserver.core.servlet.base;

import com.sinjinsong.webserver.core.enumeration.RequestMethod;
import com.sinjinsong.webserver.core.exception.ServerErrorException;
import com.sinjinsong.webserver.core.exception.ServletNotFoundException;
import com.sinjinsong.webserver.core.exception.base.ServletException;
import com.sinjinsong.webserver.core.exception.handler.ExceptionHandler;
import com.sinjinsong.webserver.core.request.Request;
import com.sinjinsong.webserver.core.resource.ResourceHandler;
import com.sinjinsong.webserver.core.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by SinjinSong on 2017/7/21.
 * Servlet运行容器
 */
@Setter
@Getter
@AllArgsConstructor
@Slf4j
public class RequestHandler implements Runnable {
    private Socket client;
    private Request request;
    private Response response;
    private HTTPServlet servlet;
    private ExceptionHandler exceptionHandler;
    private ResourceHandler resourceHandler;
    
    @Override
    public void run() {
        try {
            //为了让request能找得到response，以设置cookie
            request.setRequestHandler(this);
            //如果是静态资源，那么直接返回
            if (request.getMethod() == RequestMethod.GET && (request.getUrl().contains(".") || request.getUrl().equals("/"))) {
                log.info("静态资源:{}", request.getUrl());
                //首页
                if (request.getUrl().equals("/")) {
                    request.setUrl("/index.html");
                    resourceHandler.handle(request, response, client);
                } else {
                    resourceHandler.handle(request, response, client);
                }
            } else {
                if (servlet == null) {
                    throw new ServletNotFoundException();
                }
                //处理动态资源，交由某个Servlet执行
                //Servlet是单例多线程
                //Servlet在RequestHandler中执行
                servlet.service(request, response);
                response.write();
            }
        } catch (ServletException e) {
            exceptionHandler.handle(e, response, client);
        } catch (Exception e) {
            //其他未知异常
            exceptionHandler.handle(new ServerErrorException(), response, client);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
