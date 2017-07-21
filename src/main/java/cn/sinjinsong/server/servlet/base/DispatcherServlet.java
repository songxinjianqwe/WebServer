package cn.sinjinsong.server.servlet.base;

import cn.sinjinsong.server.WebApplication;
import cn.sinjinsong.server.enumeration.RequestMethod;
import cn.sinjinsong.server.exception.base.ServletException;
import cn.sinjinsong.server.exception.handler.ExceptionHandler;
import cn.sinjinsong.server.request.Request;
import cn.sinjinsong.server.resource.ResourceHandler;
import cn.sinjinsong.server.response.Response;
import cn.sinjinsong.server.servlet.context.ServletContext;
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
public class DispatcherServlet {
    private ResourceHandler resourceHandler;
    private ExceptionHandler exceptionHandler;
    private ThreadPoolExecutor pool;
    private ServletContext servletContext;
    
    public DispatcherServlet() throws IOException {
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
            //如果是静态资源，那么直接返回
            if (request.getMethod() == RequestMethod.GET && (request.getUrl().contains(".") || request.getUrl().equals("/"))) {
                log.info("静态资源:{}", request.getUrl());
                //首页
                if (request.getUrl().equals("/")) {
                    resourceHandler.handle("/index.html", response, client);
                } else {
                    //其他静态资源
                    //与html有关的全部放在views里
                    if (request.getUrl().endsWith(".html")) {
                        resourceHandler.handle("/views" + request.getUrl(), response, client);
                    } else {
                        //其他静态资源放在static里
                        resourceHandler.handle("/static" + request.getUrl(), response, client);
                    }
                }
            } else {
                //处理动态资源，交由某个Servlet执行
                //Servlet是单例多线程
                //Servlet在RequestHandler中执行
                pool.execute(new RequestHandler(client, request, response, servletContext.dispatch(request.getUrl()), exceptionHandler));
            }
        } catch (ServletException e) {
            exceptionHandler.handle(e, response, client);
        }
    }
}
