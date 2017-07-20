package cn.sinjinsong.server.exception.base;

import cn.sinjinsong.server.enumeration.ResponseStatus;
import lombok.Getter;

/**
 * Created by SinjinSong on 2017/7/20.
 */
@Getter
public class BaseWebException extends Exception {
    private ResponseStatus status;
    public BaseWebException(ResponseStatus status){
        this.status = status;
    }
}
