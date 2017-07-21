package cn.sinjinsong.server.servlet.base;

import cn.sinjinsong.server.enumeration.HTTPStatus;
import cn.sinjinsong.server.exception.ServerErrorException;
import cn.sinjinsong.server.exception.ServletNotFoundException;
import cn.sinjinsong.server.exception.base.ServletException;
import cn.sinjinsong.server.exception.handler.ExceptionHandler;
import cn.sinjinsong.server.request.Request;
import cn.sinjinsong.server.response.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by SinjinSong on 2017/7/21.
 * Servlet运行容器
 */
@Data
@AllArgsConstructor
@Slf4j
public class RequestHandler implements Runnable {
    private Socket client;
    private Request request;
    private Response response;
    private HTTPServlet servlet;
    private ExceptionHandler exceptionHandler;
    
    @Override
    public void run() {
        try {
            if (servlet == null) {
                throw new ServletNotFoundException(HTTPStatus.NOT_FOUND);
            }
            //为了让request能找得到response，以设置cookie
            request.setRequestHandler(this);
            servlet.service(request, response);
            response.write();
        } catch (ServletException e) {
            exceptionHandler.handle(e, response, client);
        } catch (Exception e) {
           //其他未知异常
            exceptionHandler.handle(new ServerErrorException(HTTPStatus.INTERNAL_SERVER_ERROR), response, client);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
