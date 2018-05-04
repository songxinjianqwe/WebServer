package com.sinjinsong.webserver.core.network.endpoint.nio;

import com.sinjinsong.webserver.core.network.connector.nio.IdleConnectionCleaner;
import com.sinjinsong.webserver.core.network.connector.nio.NioAcceptor;
import com.sinjinsong.webserver.core.network.connector.nio.NioPoller;
import com.sinjinsong.webserver.core.network.dispatcher.nio.NioDispatcher;
import com.sinjinsong.webserver.core.network.endpoint.Endpoint;
import com.sinjinsong.webserver.core.network.wrapper.nio.NioSocketWrapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by SinjinSong on 2017/7/20.
 */
@Slf4j
public class NioEndpoint extends Endpoint {
    
    private int pollerCount = Math.min(2, Runtime.getRuntime().availableProcessors());
    private ServerSocketChannel server;
    private NioDispatcher nioDispatcher;
    private volatile boolean isRunning = true;
    private NioAcceptor nioAcceptor;
    private List<NioPoller> nioPollers;
    private AtomicInteger pollerRotater = new AtomicInteger(0);
    /**
     * 1min
     */
    private int keepAliveTimeout = 60 * 1000 ;
    private IdleConnectionCleaner cleaner;
    
    private void initDispatcherServlet() {
        nioDispatcher = new NioDispatcher();
    }
    
    public void execute(NioSocketWrapper socketWrapper) {
        nioDispatcher.doDispatch(socketWrapper);
    }

    private void initServerSocket(int port) throws IOException {
        server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(port));
        server.configureBlocking(true);
    }

    private void initPoller() throws IOException {
        nioPollers = new ArrayList<>(pollerCount);
        for (int i = 0; i < pollerCount; i++) {
            String pollName = "NioPoller-" + i;
            NioPoller nioPoller = new NioPoller(this, pollName);
            Thread pollerThread = new Thread(nioPoller, pollName);
            pollerThread.setDaemon(true);
            pollerThread.start();
            nioPollers.add(nioPoller);
        }
    }

    /**
     * 轮询Poller，实现负载均衡
     *
     * @return
     */
    private NioPoller getPoller() {
        int idx = Math.abs(pollerRotater.incrementAndGet()) % nioPollers.size();
        return nioPollers.get(idx);
    }
    
    /**
     * 初始化Acceptor
     */
    private void initAcceptor() {
        this.nioAcceptor = new NioAcceptor(this);
        Thread t = new Thread(nioAcceptor, "NioAcceptor");
        t.setDaemon(true);
        t.start();
    }

    /**
     * 初始化IdleSocketCleaner
     */
    private void initIdleSocketCleaner() {
        cleaner = new IdleConnectionCleaner(nioPollers, keepAliveTimeout);
        cleaner.start();
    }

    @Override
    public void start(int port) {
        try {
            initDispatcherServlet();
            initServerSocket(port);
            initPoller();
            initAcceptor();
            initIdleSocketCleaner();
            log.info("服务器启动");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("初始化服务器失败");
            close();
        }
    }
    
    @Override
    public void close() {
        isRunning = false;
        cleaner.shutdown();
        for (NioPoller nioPoller : nioPollers) {
            try {
                nioPoller.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        nioDispatcher.shutdown();
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean isRunning() {
        return isRunning;
    }

    public SocketChannel serverSocketAccept() throws IOException {
        return server.accept();
    }

    /**
     * 将Acceptor接收到的socket放到随机一个Poller的Queue中
     *
     * @param socket
     * @return
     */
    public void setSocketOptions(SocketChannel socket) throws IOException {
        server.configureBlocking(false);
        getPoller().register(socket, true);
        server.configureBlocking(true);
    }

    public int getKeepAliveTimeout() {
        return this.keepAliveTimeout;
    }

}
