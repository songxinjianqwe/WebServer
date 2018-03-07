package com.sinjinsong.webserver.core;

import com.sinjinsong.webserver.core.servlet.base.DispatcherServlet;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by SinjinSong on 2017/7/20.
 */
@Slf4j
public class Server {
    private static final int DEFAULT_PORT = 8080;
    private ServerSocket server;

    private Acceptor acceptor;
    private DispatcherServlet dispatcherServlet;

    public Server() {
        this(DEFAULT_PORT);
    }

    public Server(int port) {
        try {
            server = new ServerSocket(port);
            acceptor = new Acceptor();
            acceptor.start();
            dispatcherServlet = new DispatcherServlet();
            log.info("服务器启动");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("初始化服务器失败");
            close();
        }
    }

    public void close() {
        acceptor.shutdown();
        dispatcherServlet.shutdown();
    }

    private class Acceptor extends Thread {
        @Override
        public void interrupt() {
            try {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
                super.interrupt();
            }
        }

        public void shutdown() {
            Thread.currentThread().interrupt();
        }

        @Override
        public void run() {
            log.info("开始监听");
            while (!Thread.currentThread().isInterrupted()) {
                Socket client;
                try {
                    //TCP的短连接，请求处理完即关闭
                    client = server.accept();
                    log.info("client:{}", client);
                    dispatcherServlet.doDispatch(client);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
