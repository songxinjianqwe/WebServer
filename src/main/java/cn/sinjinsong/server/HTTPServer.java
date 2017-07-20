package cn.sinjinsong.server;

import cn.sinjinsong.server.exception.RequestParseException;
import cn.sinjinsong.server.request.Request;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
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
    
    public HTTPServer() {
        try {
            server = new ServerSocket(PORT);
            pool = new ThreadPoolExecutor(5,8,1, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(10),new ThreadPoolExecutor.CallerRunsPolicy());
            listener = new Listener();
            listener.start();
            log.info("服务器启动");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void close(){
        listener.interrupt();
        pool.shutdown();
    }
    
    private class Listener extends Thread {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket client = server.accept();
                    pool.execute(new RequestHandler(client));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class RequestHandler implements Runnable {
        private Socket client;

        public RequestHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            Request request = null;
            try {
                request = new Request();
                request.build(client.getInputStream());
            } catch (RequestParseException e) {
                
            } catch (IOException e) {
                
            }
            //转发url&method对应的handler
        }
    }

    public static void main(String[] args) {
        
        HTTPServer server = new HTTPServer();
        Scanner scanner = new Scanner(System.in);
        String order = null;
        while(scanner.hasNext()){
            order = scanner.next();
            if(order.equals("EXIT")){
                server.close();
            }
        }
    }
}
