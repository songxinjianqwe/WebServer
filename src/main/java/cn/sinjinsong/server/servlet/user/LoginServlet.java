package cn.sinjinsong.server.servlet.user;

import cn.sinjinsong.server.exception.base.ServletException;
import cn.sinjinsong.server.request.Request;
import cn.sinjinsong.server.response.Response;
import cn.sinjinsong.server.service.UserService;
import cn.sinjinsong.server.servlet.base.HTTPServlet;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by SinjinSong on 2017/7/21.
 */
@Slf4j
public class LoginServlet extends HTTPServlet {
    private UserService userService;

    public LoginServlet() {
        userService = new UserService();
    }
    
    @Override
    public void doPost(Request request, Response response) throws ServletException, IOException {
        Map<String, List<String>> params = request.getParams();
        String username = params.get("username").get(0);
        String password = params.get("password").get(0);
        if (userService.login(username, password)) {
            log.info("{} 登录成功", username);
            request.getSession().setAttribute("username", username);
            request.getRequestDispatcher("/views/success.html").forward(request, response);
        } 
    }
}
