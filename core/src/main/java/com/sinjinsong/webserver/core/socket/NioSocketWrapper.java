package com.sinjinsong.webserver.core.socket;

import com.sinjinsong.webserver.core.Server;
import com.sinjinsong.webserver.core.connector.Poller;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * @author sinjinsong
 * @date 2018/3/7
 */
@Slf4j
@Data
public class NioSocketWrapper {
    private Server server;
    private SocketChannel socketChannel;
    private Poller poller;
    private boolean isNewSocket;
    private volatile long waitBegin;
    private volatile boolean isWorking;
    
    public NioSocketWrapper(Server server, SocketChannel socketChannel, Poller poller, boolean isNewSocket) {
        this.server = server;
        this.socketChannel = socketChannel;
        this.poller = poller;
        this.isNewSocket = isNewSocket;
        this.isWorking = false;
    }
    
    public void close() throws IOException {
        socketChannel.keyFor(poller.getSelector()).cancel();
        socketChannel.close();
    }
    @Override
    public String toString() {
        return socketChannel.toString();
    }
}
