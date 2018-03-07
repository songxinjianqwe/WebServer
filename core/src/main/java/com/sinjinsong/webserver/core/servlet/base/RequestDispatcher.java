package com.sinjinsong.webserver.core.servlet.base;

import com.sinjinsong.webserver.core.WebApplication;
import com.sinjinsong.webserver.core.enumeration.RequestMethod;
import com.sinjinsong.webserver.core.exception.base.ServletException;
import com.sinjinsong.webserver.core.exception.handler.ExceptionHandler;
import com.sinjinsong.webserver.core.request.Request;
import com.sinjinsong.webserver.core.resource.ResourceHandler;
import com.sinjinsong.webserver.core.response.Response;
import com.sinjinsong.webserver.core.servlet.context.ServletContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by SinjinSong on 2017/7/21.
 */
@Data
@Slf4j
public class RequestDispatcher {
    private ResourceHandler resourceHandler;
    private ExceptionHandler exceptionHandler;
    private ThreadPoolExecutor pool;
    private ServletContext servletContext;

    public RequestDispatcher() throws IOException {
        this.servletContext = WebApplication.getServletContext();
        this.exceptionHandler = new ExceptionHandler();
        this.resourceHandler = new ResourceHandler(exceptionHandler);
        this.pool = new ThreadPoolExecutor(5, 8, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public void shutdown() {
        pool.shutdown();
    }

    /**
     * 所有请求都经过DispatcherServlet的转发
     *
     * @param client
     * @throws IOException
     * @throws ServletException
     */
    public void doDispatch(Socket client) throws IOException {
         Request request = null;
        Response response = null;
        try {
            //解析请求
            request = new Request(client.getInputStream());
            response = new Response(client.getOutputStream());
            request.setServletContext(servletContext);
            pool.execute(new RequestHandler(client, request, response, servletContext.dispatch(request.getUrl()), exceptionHandler, resourceHandler));
        } catch (ServletException e) {
            exceptionHandler.handle(e, response, client);
        }
    }
}
