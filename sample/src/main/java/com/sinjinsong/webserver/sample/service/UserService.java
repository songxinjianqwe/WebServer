package com.sinjinsong.webserver.sample.service;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by SinjinSong on 2017/7/21.
 */
@Slf4j
public class UserService {
    private Map<String,String> users = new ConcurrentHashMap<>();
    private Map<String,String> online = new ConcurrentHashMap<>();
    
    public UserService(){
        users.put("admin","admin");
        users.put("user1","pwd1");
    }
    
    public boolean login(String username,String password){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String val = users.get(username);
        if(password.equals(val)) {
            online.put(username,"");
            return true;
        }
        return false;
    }
    
}
