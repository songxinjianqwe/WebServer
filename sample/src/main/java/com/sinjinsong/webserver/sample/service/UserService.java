package com.sinjinsong.webserver.sample.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by SinjinSong on 2017/7/21.
 */
public class UserService {
    private Map<String,String> users = new ConcurrentHashMap<>();
    private Map<String,String> online = new ConcurrentHashMap<>();
    
    public UserService(){
        users.put("admin","admin");
        users.put("user1","pwd1");
    }
    
    public boolean login(String username,String password){
        if(users.containsKey(username) && users.get(username).equals(password)){
            online.put(username,"");
            return true;
        }
        return false;
    }
    
}
