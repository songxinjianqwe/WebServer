package cn.sinjinsong.server.request.dispatcher;

import cn.sinjinsong.server.exception.base.ServletException;
import cn.sinjinsong.server.request.Request;
import cn.sinjinsong.server.response.Response;

import java.io.IOException;

/**
 * Created by SinjinSong on 2017/7/21.
 */
public interface RequestDispatcher {
    
    void forward(Request request, Response response)  throws ServletException, IOException;
}
