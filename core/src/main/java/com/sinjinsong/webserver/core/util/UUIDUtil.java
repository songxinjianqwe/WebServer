package com.sinjinsong.webserver.core.util;

import java.util.UUID;

/**
 * Created by SinjinSong on 2017/7/21.
 */
public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }
}
