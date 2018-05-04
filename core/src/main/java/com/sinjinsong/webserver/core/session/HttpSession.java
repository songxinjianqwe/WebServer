package com.sinjinsong.webserver.core.session;


import com.sinjinsong.webserver.core.context.WebApplication;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by SinjinSong on 2017/7/21.
 */
public class HttpSession {
    private String id;
    private Map<String, Object> attributes;
    private boolean isValid;
    private Instant lastAccessed;


    public HttpSession(String id) {
        this.id = id;
        this.attributes = new ConcurrentHashMap<>();
        this.isValid = true;
        this.lastAccessed = Instant.now();
    }

    public void invalidate() {
        this.isValid = false;
        this.attributes.clear();
        WebApplication.getServletContext().invalidateSession(this);
    }

    public Object getAttribute(String key) {
        if (isValid) {
            this.lastAccessed = Instant.now();
            return attributes.get(key);
        }
        throw new IllegalStateException("session has invalidated");
    }

    public void setAttribute(String key, Object value) {
        if (isValid) {
            this.lastAccessed = Instant.now();
            attributes.put(key, value);
        } else {
            throw new IllegalStateException("session has invalidated");
        }
    }

    public String getId() {
        return id;
    }

    public Instant getLastAccessed() {
        return lastAccessed;
    }


    public void removeAttribute(String key) {
        attributes.remove(key);
    }
    
    
}
