package com.sinjinsong.webserver.core.exception;

import com.sinjinsong.webserver.core.enumeration.HttpStatus;
import com.sinjinsong.webserver.core.exception.base.ServletException;

/**
 * Created by SinjinSong on 2017/7/21.
 * 静态资源未找到
 */
public class ResourceNotFoundException extends ServletException {
    private static final HttpStatus status = HttpStatus.NOT_FOUND;
    public ResourceNotFoundException() {
        super(status);
    }
}
