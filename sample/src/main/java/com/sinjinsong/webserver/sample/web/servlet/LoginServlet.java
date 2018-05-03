package com.sinjinsong.webserver.sample.web.servlet;

import com.sinjinsong.webserver.core.exception.base.ServletException;
import com.sinjinsong.webserver.core.request.Request;
import com.sinjinsong.webserver.core.response.Response;
import com.sinjinsong.webserver.sample.service.UserService;
import com.sinjinsong.webserver.core.servlet.impl.HttpServlet;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by SinjinSong on 2017/7/21.
 */
@Slf4j
public class LoginServlet extends HttpServlet {
    
    private UserService userService;

    public LoginServlet() {
        userService = UserService.getInstance();
    }

    @Override
    public void init() {
        log.info("LoginServlet init...");
    }

    @Override
    public void destroy() {
        log.info("LoginServlet destroy...");
    }

    @Override
    public void doGet(Request request, Response response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");
        if (username != null) {
            log.info("已经登录，跳转至success页面");
            response.sendRedirect("/views/success.html");
        } else {
            request.getRequestDispatcher("/views/login.html").forward(request,response);
        }
    }

    @Override
    public void doPost(Request request, Response response) throws ServletException, IOException {
        Map<String, List<String>> params = request.getParams();
        String username = params.get("username").get(0);
        String password = params.get("password").get(0);
        if (userService.login(username, password)) {
            log.info("{} 登录成功", username);
            request.getSession().setAttribute("username", username);
            response.sendRedirect("/views/success.html");
        } else {
            log.info("登录失败");
            response.sendRedirect("/views/errors/400.html");
        }
    }
}
