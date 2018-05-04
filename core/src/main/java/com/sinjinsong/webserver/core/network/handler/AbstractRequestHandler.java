package com.sinjinsong.webserver.core.network.handler;

import com.sinjinsong.webserver.core.context.ServletContext;
import com.sinjinsong.webserver.core.exception.FilterNotFoundException;
import com.sinjinsong.webserver.core.exception.ServerErrorException;
import com.sinjinsong.webserver.core.exception.ServletNotFoundException;
import com.sinjinsong.webserver.core.exception.base.ServletException;
import com.sinjinsong.webserver.core.exception.handler.ExceptionHandler;
import com.sinjinsong.webserver.core.filter.Filter;
import com.sinjinsong.webserver.core.filter.FilterChain;
import com.sinjinsong.webserver.core.request.Request;
import com.sinjinsong.webserver.core.resource.ResourceHandler;
import com.sinjinsong.webserver.core.response.Response;
import com.sinjinsong.webserver.core.servlet.Servlet;
import com.sinjinsong.webserver.core.network.wrapper.SocketWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author sinjinsong
 * @date 2018/5/4
 */
@Slf4j
@Getter
public abstract class AbstractRequestHandler implements FilterChain, Runnable {

    protected Request request;
    protected Response response;
    protected SocketWrapper socketWrapper;
    protected ServletContext servletContext;
    protected ExceptionHandler exceptionHandler;
    protected ResourceHandler resourceHandler;
    protected boolean isFinished;
    protected Servlet servlet;
    protected List<Filter> filters;
    private int filterIndex = 0;

    public AbstractRequestHandler(SocketWrapper socketWrapper, ServletContext servletContext, ExceptionHandler exceptionHandler, ResourceHandler resourceHandler, Request request, Response response) throws ServletNotFoundException, FilterNotFoundException {
        this.socketWrapper = socketWrapper;
        this.servletContext = servletContext;
        this.exceptionHandler = exceptionHandler;
        this.resourceHandler = resourceHandler;
        this.isFinished = false;
        this.request = request;
        this.response = response;
        request.setServletContext(servletContext);
        request.setRequestHandler(this);
        response.setRequestHandler(this);
        servlet = servletContext.mapServlet(request.getUrl());
        filters = servletContext.mapFilter(request.getUrl());
    }

    @Override
    public void run() {
        if (filters.isEmpty()) {
            service();
        } else {
            doFilter(request, response);
        }
    }
   
    @Override
    public void doFilter(Request request, Response response) {
        if (filterIndex < filters.size()) {
            filters.get(filterIndex++).doFilter(request, response, this);
        } else {
            service();
        }
    }

    private void service() {
        log.info("socket isClosed: {}",this.socketWrapper.isClosed());
        try {
            //处理动态资源，交由某个Servlet执行
            //Servlet是单例多线程
            //Servlet在RequestHandler中执行
            servlet.service(request, response);
        } catch (ServletException e) {
            exceptionHandler.handle(e, response, socketWrapper);
        } catch (Exception e) {
            //其他未知异常
            e.printStackTrace();
            exceptionHandler.handle(new ServerErrorException(), response, socketWrapper);
        } finally {
            if (!isFinished) {
                finishRequest();
            }
        }
        log.info("请求处理完毕");
    }

    public abstract void finishRequest();
}
