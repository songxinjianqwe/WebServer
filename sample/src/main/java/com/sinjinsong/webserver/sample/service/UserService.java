package com.sinjinsong.webserver.sample.service;

import com.sinjinsong.webserver.sample.domain.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by SinjinSong on 2017/7/21.
 */
@Slf4j
public class UserService {
    private static UserService instance = new UserService();

    public static UserService getInstance() {
        return instance;
    }

    private Map<String, User> users = new ConcurrentHashMap<>();
    private Map<String, String> online = new ConcurrentHashMap<>();

    
    public UserService() {
        users.put("admin", new User("admin", "admin", "管理员", 20));
        users.put("user1", new User("user1", "pwd1", "用户1", 23));
    }

    public boolean login(String username, String password) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        User user = users.get(username);
        if (password.equals(user.getPassword())) {
            online.put(username, "");
            return true;
        }
        return false;
    }

    public User findByUsername(String username) {
        return users.get(username);
    }
    
    
    public void update(User user) {
        users.put(user.getUsername(),user);
    }
}
