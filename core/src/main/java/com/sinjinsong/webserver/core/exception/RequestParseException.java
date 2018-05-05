package com.sinjinsong.webserver.core.exception;

import com.sinjinsong.webserver.core.enumeration.HttpStatus;
import com.sinjinsong.webserver.core.exception.base.ServletException;

/**
 * Created by SinjinSong on 2017/7/20.
 * 请求解析出国
 */
public class RequestParseException extends ServletException {
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;
    public RequestParseException() {
        super(status);
    }
}
