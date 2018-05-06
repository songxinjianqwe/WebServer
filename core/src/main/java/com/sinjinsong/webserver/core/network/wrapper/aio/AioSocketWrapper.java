package com.sinjinsong.webserver.core.network.wrapper.aio;

import com.sinjinsong.webserver.core.network.endpoint.aio.AioEndpoint;
import com.sinjinsong.webserver.core.network.wrapper.SocketWrapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author sinjinsong
 * @date 2018/3/7
 */
@Slf4j
@Data
public class AioSocketWrapper implements SocketWrapper {
    private AioEndpoint server;
    private AsynchronousSocketChannel socketChannel;
    private volatile long waitBegin;
    private volatile boolean isWorking;
    
    public AioSocketWrapper(AioEndpoint server, AsynchronousSocketChannel socketChannel) {
        this.server = server;
        this.socketChannel = socketChannel;
        this.isWorking = false;
    }
    
    public void close() throws IOException {
        socketChannel.close();
    }


    @Override
    public String toString() {
        return socketChannel.toString();
    }
}
