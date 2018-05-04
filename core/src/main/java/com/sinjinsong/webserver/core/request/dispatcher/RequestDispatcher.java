package com.sinjinsong.webserver.core.request.dispatcher;

import com.sinjinsong.webserver.core.exception.base.ServletException;
import com.sinjinsong.webserver.core.request.Request;
import com.sinjinsong.webserver.core.response.Response;

import java.io.IOException;

/**
 * Created by SinjinSong on 2017/7/21.
 */
public interface RequestDispatcher {
    
    void forward(Request request, Response response) throws ServletException, IOException;
}
