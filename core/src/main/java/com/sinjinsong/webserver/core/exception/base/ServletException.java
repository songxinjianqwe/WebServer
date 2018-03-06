package com.sinjinsong.webserver.core.exception.base;

import com.sinjinsong.webserver.core.enumeration.HTTPStatus;
import lombok.Getter;

/**
 * Created by SinjinSong on 2017/7/20.
 */
@Getter
public class ServletException extends Exception {
    private HTTPStatus status;
    public ServletException(HTTPStatus status){
        this.status = status;
    }
}
