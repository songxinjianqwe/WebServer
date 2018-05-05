package com.sinjinsong.webserver.core.exception;

import com.sinjinsong.webserver.core.enumeration.HttpStatus;
import com.sinjinsong.webserver.core.exception.base.ServletException;

/**
 * Created by SinjinSong on 2017/7/21.
 * 500异常
 */
public class ServerErrorException extends ServletException{
    private static final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    public ServerErrorException() {
        super(status);
    }
}
