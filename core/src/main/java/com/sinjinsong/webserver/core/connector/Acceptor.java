package com.sinjinsong.webserver.core.connector;

import com.sinjinsong.webserver.core.wrapper.AioSocketWrapper;
import lombok.extern.slf4j.Slf4j;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author sinjinsong
 * @date 2018/5/4
 */
@Slf4j
public class Acceptor implements CompletionHandler<AsynchronousSocketChannel, Void> {
    private Server server;

    public Acceptor(Server server) {
        this.server = server;
    }


    @Override
    public void completed(AsynchronousSocketChannel result, Void attachment) {
        server.accept();
        server.execute(new AioSocketWrapper(server, result));
    }

    @Override
    public void failed(Throwable e, Void attachment) {
        log.info("accept failed...");
        e.printStackTrace();
    }
}
