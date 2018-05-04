package com.sinjinsong.webserver.core.servlet;

import com.sinjinsong.webserver.core.exception.ServerErrorException;
import com.sinjinsong.webserver.core.exception.base.ServletException;
import com.sinjinsong.webserver.core.exception.handler.ExceptionHandler;
import com.sinjinsong.webserver.core.filter.Filter;
import com.sinjinsong.webserver.core.filter.FilterChain;
import com.sinjinsong.webserver.core.request.Request;
import com.sinjinsong.webserver.core.resource.ResourceHandler;
import com.sinjinsong.webserver.core.response.Response;
import com.sinjinsong.webserver.core.wrapper.AioSocketWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.nio.channels.CompletionHandler;
import java.util.List;

/**
 * Created by SinjinSong on 2017/7/21.
 * Servlet运行容器
 */
@Setter
@Getter
@Slf4j
@AllArgsConstructor
public class RequestHandler implements Runnable, FilterChain {
    private AioSocketWrapper socketWrapper;
    private Request request;
    private Response response;
    private Servlet servlet;
    private List<Filter> filters;
    private ExceptionHandler exceptionHandler;
    private ResourceHandler resourceHandler;
    private int filterIndex = 0;
    private boolean isFinished;
    private CompletionHandler readHandler;
    
    public RequestHandler(AioSocketWrapper socketWrapper, Request request, Response response, Servlet servlet, List<Filter> filters, ExceptionHandler exceptionHandler, ResourceHandler resourceHandler,CompletionHandler readHandler) {
        this.socketWrapper = socketWrapper;
        this.request = request;
        this.response = response;
        this.servlet = servlet;
        this.filters = filters;
        this.exceptionHandler = exceptionHandler;
        this.resourceHandler = resourceHandler;
        this.isFinished = false;
        this.readHandler = readHandler;
    }

    /**
     * 在这里处理keep-alive
     */
    @Override
    public void run() {
        log.info("RequestHandler running...");
        //为了让request能找得到response，以设置cookie
        request.setRequestHandler(this);
        response.setRequestHandler(this);
        if (filters.isEmpty()) {
            service();
        } else {
            doFilter(request, response);
        }
        log.info("RequestHandler finished...");
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
            if(!isFinished) {
                finishRequest();
            }
        }
        log.info("请求处理完毕");
    }
    
    
    public void finishRequest() {
        isFinished = true;
        response.writeToClient(readHandler);
        com.sinjinsong.webserver.core.server.WebApplication.getServletContext().afterRequestDestroyed(request);   
    }
}
