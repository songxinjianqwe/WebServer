package com.sinjinsong.webserver.core.exception;

import com.sinjinsong.webserver.core.enumeration.HttpStatus;
import com.sinjinsong.webserver.core.exception.base.ServletException;

/**
 * Created by SinjinSong on 2017/7/21.
 * 未找到对应的Servlet（web.xml配置错误）
 */
public class ServletNotFoundException extends ServletException {
    private static final HttpStatus status = HttpStatus.NOT_FOUND;
    public ServletNotFoundException() {
        super(status);
    }
}
