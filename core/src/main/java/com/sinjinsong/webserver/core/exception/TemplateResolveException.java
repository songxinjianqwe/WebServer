package com.sinjinsong.webserver.core.exception;

import com.sinjinsong.webserver.core.enumeration.HttpStatus;
import com.sinjinsong.webserver.core.exception.base.ServletException;

/**
 * Created by SinjinSong on 2017/7/21.
 * 模板引擎解析错误（html文件编写错误）
 */
public class TemplateResolveException extends ServletException {
    private static final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    public TemplateResolveException() {
        super(status);
    }
}
   
