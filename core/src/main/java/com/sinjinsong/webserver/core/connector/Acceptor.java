package com.sinjinsong.webserver.core.connector;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SocketChannel;

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
        log.info("{} 开始监听",Thread.currentThread().getName());
        while (server.isRunning()) {
            SocketChannel client;
            try {
                client = server.serverSocketAccept();
                if(client == null){
                    continue;
                }
                client.configureBlocking(false);
                log.info("Acceptor接收到连接请求 {}",client);
                server.setSocketOptions(client); 
                log.info("socketWrapper:{}", client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
