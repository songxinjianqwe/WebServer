package com.sinjinsong.webserver.core;

import com.sinjinsong.webserver.core.servlet.base.DispatcherServlet;
import com.sinjinsong.webserver.core.thread.Acceptor;
import com.sinjinsong.webserver.core.thread.Poller;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by SinjinSong on 2017/7/20.
 */
@Slf4j
public class Server {
    private static final int DEFAULT_PORT = 8080;
    private int acceptorCount = 2;
    private int pollerCount = Math.min(2, Runtime.getRuntime().availableProcessors());
    private ServerSocketChannel server;
    private DispatcherServlet dispatcherServlet;
    private volatile boolean isRunning = true;
    protected List<Acceptor> acceptors;
    private List<Poller> pollers;
    private AtomicInteger pollerRotater = new AtomicInteger(0);

    public Server() {
        this(DEFAULT_PORT);
    }

    public Server(int port) {
        try {
            initServerSocket(port);
            initAcceptor();
            initPoller();
            dispatcherServlet = new DispatcherServlet();
            log.info("服务器启动");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initServerSocket(int port) throws IOException {
        server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(port));
        server.configureBlocking(true);
    }

    private void initPoller() {
        pollers = new ArrayList<>(pollerCount);
        for (int i = 0; i < pollerCount; i++) {
            Poller poller = new Poller(this);
            String pollName = "-ClientPoller-" + i;
            Thread pollerThread = new Thread(poller, pollName);
            pollerThread.setDaemon(true);
            pollerThread.start();
            pollers.add(poller);
        }
    }

    public Poller getPoller() {
        int idx = Math.abs(pollerRotater.incrementAndGet()) % pollers.size();
        return pollers.get(idx);
    }
    
    private void initAcceptor() {
        acceptors = new ArrayList<>(acceptorCount);
        for (int i = 0; i < acceptorCount; i++) {
            Acceptor acceptor = new Acceptor(this);
            String threadName = "Acceptor-" + i;
            Thread t = new Thread(acceptor, threadName);
            t.setDaemon(true);
            t.start();
            acceptors.add(acceptor);
        }
    }

    public void close() {
//        acceptor.shutdown();
        // poolers.shutdown();
        isRunning = false;
        dispatcherServlet.shutdown();
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    
    public Socket serverSocketAccept() throws IOException {
        return server.accept();
    }
    
}
