package com.sinjinsong.webserver.core.network.dispatcher.bio;

import com.sinjinsong.webserver.core.exception.RequestInvalidException;
import com.sinjinsong.webserver.core.exception.base.ServletException;
import com.sinjinsong.webserver.core.network.dispatcher.AbstractDispatcher;
import com.sinjinsong.webserver.core.network.handler.bio.BioRequestHandler;
import com.sinjinsong.webserver.core.network.wrapper.SocketWrapper;
import com.sinjinsong.webserver.core.network.wrapper.bio.BioSocketWrapper;
import com.sinjinsong.webserver.core.request.Request;
import com.sinjinsong.webserver.core.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author sinjinsong
 * @date 2018/5/4
 */
@Slf4j
public class BioDispatcher extends AbstractDispatcher {
    
    @Override
    public void doDispatch(SocketWrapper socketWrapper) {
        BioSocketWrapper bioSocketWrapper = (BioSocketWrapper) socketWrapper;
        Socket socket = bioSocketWrapper.getSocket();
        Request request = null;
        Response response = null;
        try {
            BufferedInputStream bin = new BufferedInputStream(socket.getInputStream());
            byte[] buf = null;
            try {
                buf = new byte[bin.available()];
                int len = bin.read(buf);
                if (len <= 0) {
                    throw new RequestInvalidException();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 这里这里不要把in关掉，把in关掉就等同于把socket关掉
            //解析请求
            response = new Response();
            request = new Request(buf);
            pool.execute(new BioRequestHandler(socketWrapper, servletContext, exceptionHandler, resourceHandler, request, response));
        } catch (ServletException e) {
            exceptionHandler.handle(e, response, socketWrapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
