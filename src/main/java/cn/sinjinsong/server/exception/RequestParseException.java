package cn.sinjinsong.server.exception;

import cn.sinjinsong.server.enumeration.ResponseStatus;
import cn.sinjinsong.server.exception.base.BaseWebException;

/**
 * Created by SinjinSong on 2017/7/20.
 */
public class RequestParseException extends BaseWebException {

    public RequestParseException(ResponseStatus status) {
        super(status);
    }
}
