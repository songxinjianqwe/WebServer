package cn.sinjinsong.server.exception;

import cn.sinjinsong.server.enumeration.HTTPStatus;
import cn.sinjinsong.server.exception.base.ServletException;

/**
 * Created by SinjinSong on 2017/7/21.
 */
public class ResourceNotFoundException extends ServletException {
    private static final HTTPStatus status = HTTPStatus.NOT_FOUND;
    public ResourceNotFoundException() {
        super(status);
    }
}
