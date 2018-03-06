package com.sinjinsong.webserver.core.thread;

import com.sinjinsong.webserver.core.Server;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

/**
 * @author sinjinsong
 * @date 2018/3/6
 */
@Slf4j
public class Acceptor implements Runnable {
    private Server server;

    public Acceptor(Server server) {
        this.server = server;
    }
    
    @Override
    public void run() {
        log.info("开始监听");
        while (server.isRunning()) {
            Socket client;
            try {
                //TCP的短连接，请求处理完即关闭
                client = server.serverSocketAccept();

                log.info("client:{}", client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
