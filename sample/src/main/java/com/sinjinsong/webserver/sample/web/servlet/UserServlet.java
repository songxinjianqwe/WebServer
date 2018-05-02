package com.sinjinsong.webserver.sample.web.servlet;

import com.sinjinsong.webserver.core.exception.base.ServletException;
import com.sinjinsong.webserver.core.request.Request;
import com.sinjinsong.webserver.core.response.Response;
import com.sinjinsong.webserver.core.servlet.base.HTTPServlet;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Created by SinjinSong on 2017/7/21.
 */
@Slf4j
public class UserServlet extends HTTPServlet {
    @Override
    public void doGet(Request request, Response response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/user.html").forward(request, response);
    }
}
