package com.sinjinsong.webserver.core.servlet;

import com.sinjinsong.webserver.core.context.ServletContext;
import com.sinjinsong.webserver.core.exception.base.ServletException;
import com.sinjinsong.webserver.core.exception.handler.ExceptionHandler;
import com.sinjinsong.webserver.core.request.Request;
import com.sinjinsong.webserver.core.resource.ResourceHandler;
import com.sinjinsong.webserver.core.response.Response;
import com.sinjinsong.webserver.core.wrapper.AioSocketWrapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

/**
 * Created by SinjinSong on 2017/7/21.
 */
@Data
@Slf4j
public class DispatcherServlet {
    private ResourceHandler resourceHandler;
    private ExceptionHandler exceptionHandler;
    private ServletContext servletContext;

    public DispatcherServlet() {
        this.servletContext = com.sinjinsong.webserver.core.server.WebApplication.getServletContext();
        this.exceptionHandler = new ExceptionHandler();
        this.resourceHandler = new ResourceHandler(exceptionHandler);
    }

    public void shutdown() {
        servletContext.destroy();
    }

    /**
     * 所有请求都经过DispatcherServlet的转发
     * 读取
     * @throws IOException
     * @throws ServletException
     */
    public void doDispatch(AioSocketWrapper socketWrapper) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        socketWrapper.getSocketChannel().read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                Request request = null;
                Response response = null;
                try {
                    //解析请求
                    request = new Request(attachment.array());
                    response = new Response(socketWrapper.getSocketChannel());
                    request.setServletContext(servletContext);
                    new RequestHandler(socketWrapper, request, response, servletContext.mapServlet(request.getUrl()), servletContext.mapFilter(request.getUrl()), exceptionHandler, resourceHandler,this).run();
                } catch (ServletException e) {
                    exceptionHandler.handle(e, response, socketWrapper);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            @Override
            public void failed(Throwable e, ByteBuffer attachment) {
                log.error("read failed");
                e.printStackTrace();
            }
        });
    }
}
