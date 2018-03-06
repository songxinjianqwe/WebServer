package com.sinjinsong.webserver.core;

import java.util.Scanner;

/**
 * @author sinjinsong
 * @date 2018/3/6
 */
public class BootStrap {
    
    public static void run() {
        Server server = new Server();
        Scanner scanner = new Scanner(System.in);
        String order = null;
        while (scanner.hasNext()) {
            order = scanner.next();
            if (order.equals("EXIT")) {
                server.close();
                System.exit(0);
            }
        }
    }
}
