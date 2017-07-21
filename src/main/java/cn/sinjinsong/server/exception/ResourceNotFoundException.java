package cn.sinjinsong.server.exception;

import cn.sinjinsong.server.enumeration.HTTPStatus;
import cn.sinjinsong.server.exception.base.ServletException;

/**
 * Created by SinjinSong on 2017/7/21.
 */
public class ResourceNotFoundException extends ServletException {
    public ResourceNotFoundException(HTTPStatus status) {
        super(status);
    }
}
