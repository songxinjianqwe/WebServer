package com.sinjinsong.webserver.core.network.connector.nio;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author sinjinsong
 * @date 2018/5/2
 */
@Slf4j
public class IdleConnectionCleaner implements Runnable {
    private ScheduledExecutorService executor;
    private List<NioPoller> nioPollers;

    public IdleConnectionCleaner(List<NioPoller> nioPollers) {
        this.nioPollers = nioPollers;
    }

    public void start() {
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "IdleConnectionCleaner");
            }
        };
        executor = Executors.newSingleThreadScheduledExecutor(threadFactory);
        executor.scheduleWithFixedDelay(this, 0, 5, TimeUnit.SECONDS);
    }

    public void shutdown() {
        executor.shutdown();
    }

    @Override
    public void run() {
        for (NioPoller nioPoller : nioPollers) {
            log.info("Cleaner 检测{} 所持有的Socket中...", nioPoller.getPollerName());
            nioPoller.cleanTimeoutSockets();
        }
        log.info("检测结束...");
    }
}
