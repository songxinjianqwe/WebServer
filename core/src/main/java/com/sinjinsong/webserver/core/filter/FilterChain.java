package com.sinjinsong.webserver.core.filter;

import com.sinjinsong.webserver.core.request.Request;
import com.sinjinsong.webserver.core.response.Response;

/**
 * @author sinjinsong
 * @date 2018/5/2
 * 拦截器链
 */
public interface FilterChain {
    /**
     * 当前filter放行，由后续的filter继续进行过滤
     * @param request
     * @param response
     */
    void doFilter(Request request,Response response) ;
}
