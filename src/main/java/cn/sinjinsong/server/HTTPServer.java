package cn.sinjinsong.server;

import cn.sinjinsong.server.servlet.base.DispatcherServlet;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by SinjinSong on 2017/7/20.
 */
@Slf4j
public class HTTPServer {
    private static final int PORT = 8000;
    private ServerSocket server;
    
    private Listener listener;
    private DispatcherServlet dispatcherServlet;
    public HTTPServer() {
        try {
            server = new ServerSocket(PORT);
            listener = new Listener();
            listener.start();
            dispatcherServlet = new DispatcherServlet();
            log.info("服务器启动");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        listener.shutdown();
        dispatcherServlet.shutdown();
    }

    private class Listener extends Thread {
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
    

    public static void main(String[] args) {

        HTTPServer server = new HTTPServer();
        Scanner scanner = new Scanner(System.in);
        String order = null;
        while (scanner.hasNext()) {
            order = scanner.next();
            if (order.equals("EXIT")) {
                server.close();
                System.exit(0);
            }
        }
    }
}
