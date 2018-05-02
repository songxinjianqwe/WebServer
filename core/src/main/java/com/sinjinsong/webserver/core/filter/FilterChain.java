package com.sinjinsong.webserver.core.filter;

import com.sinjinsong.webserver.core.request.Request;
import com.sinjinsong.webserver.core.response.Response;

/**
 * @author sinjinsong
 * @date 2018/5/2
 */
public interface FilterChain {
    void doFilter(Request request,Response response) ;
}
