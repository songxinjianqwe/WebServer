package com.sinjinsong.webserver.core.thread;

import com.sinjinsong.webserver.core.Server;

/**
 * @author sinjinsong
 * @date 2018/3/6
 */
public class Poller implements Runnable {
    private Server server;

    public Poller(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
//        dispatcherServlet.doDispatch(client);
    }
}
