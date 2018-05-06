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
 * RequestHandler 的父类，通过父类来复用成员变量和部分方法
 * 不同IO模型的RequestHandler基本是在将Response写回客户端这部分有不同的实现，在这里被设置为了抽象方法
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
        // 根据url查询匹配的servlet，结果是0个或1个
        servlet = servletContext.mapServlet(request.getUrl());
        // 根据url查询匹配的filter，结果是0个或多个
        filters = servletContext.mapFilter(request.getUrl());
    }

    /**
     * 入口
     */
    @Override
    public void run() {
        // 如果没有filter，则直接执行servlet
        if (filters.isEmpty()) {
            service();
        } else {
            // 先执行filter
            doFilter(request, response);
        }
    }

    /**
     * 递归执行，自定义filter中如果同意放行，那么会调用filterChain(也就是requestHandler)的doiFilter方法，
     * 此时会执行下一个filter的doFilter方法；
     * 如果不放行，那么会在sendRedirect之后将响应数据写回客户端，结束；
     * 如果所有Filter都执行完毕，那么会调用service方法，执行servlet逻辑
     * @param request
     * @param response
     */
    @Override
    public void doFilter(Request request, Response response) {
        if (filterIndex < filters.size()) {
            filters.get(filterIndex++).doFilter(request, response, this);
        } else {
            service();
        }
    }

    /**
     * 调用servlet
     */
    private void service() {
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
                flushResponse();
            }
        }
        log.info("请求处理完毕");
    }

    /**
     * 响应数据写回到客户端
     */
    public abstract void flushResponse();
}
