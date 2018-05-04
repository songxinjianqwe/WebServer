package com.sinjinsong.webserver.core.connector;

import com.sinjinsong.webserver.core.servlet.DispatcherServlet;
import com.sinjinsong.webserver.core.wrapper.AioSocketWrapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.*;

/**
 * Created by SinjinSong on 2017/7/20.
 */
@Slf4j
public class Server {

    private static final int DEFAULT_PORT = 8080;
    private AsynchronousServerSocketChannel server;
    private DispatcherServlet dispatcherServlet;
    private Acceptor acceptor;
    private int keepAliveTimeout = 5000;
    private ExecutorService pool;
    
    public Server() {
        this(DEFAULT_PORT);
    }

    public Server(int port) {
        try {
            initDispatcherServlet();
            initServerSocket(port);
            log.info("服务器启动");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("初始化服务器失败");
            close();
        }
    }

    private void initDispatcherServlet() {
        dispatcherServlet = new DispatcherServlet();
    }


    public void execute(AioSocketWrapper socketWrapper) {
        dispatcherServlet.doDispatch(socketWrapper);
    }

    private void initServerSocket(int port) throws IOException {
        ThreadFactory threadFactory = new ThreadFactory() {
            private int count;
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Worker Pool-" + count++);
            }
        };
        pool = new ThreadPoolExecutor(100, 100, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(200), threadFactory,new ThreadPoolExecutor.CallerRunsPolicy());
        // 以指定线程池来创建一个AsynchronousChannelGroup  
        AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup
                .withThreadPool(pool);
        // 以指定线程池来创建一个AsynchronousServerSocketChannel  
        server = AsynchronousServerSocketChannel.open(channelGroup)
                // 指定监听本机的PORT端口  
                .bind(new InetSocketAddress(port));
        // 使用CompletionHandler接受来自客户端的连接请求  
        acceptor = new Acceptor(this);
        server.accept(null, acceptor);    
    }

    public void accept() {
        server.accept(null,acceptor);
    }

    public void close() {
        
        dispatcherServlet.shutdown();
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public int getKeepAliveTimeout() {
        return this.keepAliveTimeout;
    }

}
