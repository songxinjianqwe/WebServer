package cn.sinjinsong.server.util;

import java.util.UUID;

/**
 * Created by SinjinSong on 2017/7/21.
 */
public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }
}
