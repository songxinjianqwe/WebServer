package cn.sinjinsong.server.enumeration;

/**
 * Created by SinjinSong on 2017/7/20.
 */
public enum ResponseStatus {
    OK(200),NOT_FOUND(404),INTERNAL_SERVER_ERROR(500),BAD_REQUEST(400);
    private int code;
    ResponseStatus(int code){
        this.code = code;
    }
    public int getCode(){
        return code;
    }
}
