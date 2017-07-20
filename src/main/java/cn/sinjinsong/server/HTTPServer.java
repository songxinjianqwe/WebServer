package cn.sinjinsong.server;

import cn.sinjinsong.server.enumeration.RequestMethod;
import cn.sinjinsong.server.exception.base.BaseWebException;
import cn.sinjinsong.server.exception.handler.ExceptionHandler;
import cn.sinjinsong.server.request.Request;
import cn.sinjinsong.server.resource.ResourceHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by SinjinSong on 2017/7/20.
 */
@Slf4j
public class HTTPServer {
    private static final int PORT = 8000;
    private ServerSocket server;
    private ThreadPoolExecutor pool;
    private Listener listener;
    private ExceptionHandler exceptionHandler;
    private ResourceHandler resourceHandler;

    public HTTPServer() {
        try {
            server = new ServerSocket(PORT);
            pool = new ThreadPoolExecutor(5, 8, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10), new ThreadPoolExecutor.CallerRunsPolicy());
            listener = new Listener();
            listener.start();
            exceptionHandler = new ExceptionHandler();
            resourceHandler = new ResourceHandler(exceptionHandler);
            log.info("服务器启动");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        listener.shutdown();
        pool.shutdown();
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
                Socket client = null;
                try {
                    //TCP的短连接，请求处理完即关闭
                    client = server.accept();
                    log.info("client:{}", client);
                    pool.execute(new RequestHandler(client));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private class RequestHandler implements Runnable {
        private Socket client;
        private InputStream in;
        private OutputStream out;

        public RequestHandler(Socket client) throws IOException {
            this.client = client;
            this.in = client.getInputStream();
            this.out = client.getOutputStream();
        }

        @Override
        public void run() {
            try {
                //解析请求
                Request request = null;
                request = new Request();
                request.build(in);
                log.info("Request:\n{}",request);
                //如果是静态资源，那么直接返回
                if (request.getMethod() == RequestMethod.GET && (request.getUrl().contains(".") || request.getUrl().equals("/"))) {
                    log.info("静态资源:{}", request.getUrl());
                    //首页
                    if (request.getUrl().equals("/")) {
                        resourceHandler.handle("/index.html", out);
                    } else {
                        //其他静态资源
                        //与html有关的全部放在views里
                        if (request.getUrl().endsWith(".html")) {
                            resourceHandler.handle("/views" + request.getUrl(), out);
                        } else {
                            //其他静态资源放在static里
                            resourceHandler.handle("/static" + request.getUrl(), out);
                        }
                    }
                }
                //转发url&method对应的handler
                
            } catch (BaseWebException e) {
                log.info("抛出异常:{}", e.getClass().getName());
                e.printStackTrace();
                exceptionHandler.handle(e, out);
            } finally {
                try {
                    client.close();
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
