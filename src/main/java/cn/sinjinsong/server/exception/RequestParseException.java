package cn.sinjinsong.server.exception;

import cn.sinjinsong.server.enumeration.HTTPStatus;
import cn.sinjinsong.server.exception.base.ServletException;

/**
 * Created by SinjinSong on 2017/7/20.
 */
public class RequestParseException extends ServletException {
    private static final HTTPStatus status = HTTPStatus.BAD_REQUEST;
    public RequestParseException() {
        super(status);
    }
}
