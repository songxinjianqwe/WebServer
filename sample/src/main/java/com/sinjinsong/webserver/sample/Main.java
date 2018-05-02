package com.sinjinsong.webserver.sample;

import com.sinjinsong.webserver.core.BootStrap;

/**
 * @author sinjinsong
 * @date 2018/3/6
 */
public class Main {
    public static void main(String[] args) {
        BootStrap.run(args.length > 0 ? args[0] : null);
    }
}
