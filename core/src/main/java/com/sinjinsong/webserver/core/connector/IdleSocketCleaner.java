package com.sinjinsong.webserver.core.connector;

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
public class IdleSocketCleaner implements Runnable {
    private ScheduledExecutorService executor;
    private List<Poller> pollers;
    private int keepAliveTimeout;

    public IdleSocketCleaner(List<Poller> pollers, int keepAliveTimeout) {
        this.pollers = pollers;
        this.keepAliveTimeout = keepAliveTimeout;
    }

    public void start() {
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "IdleSocketCleaner");
            }
        };
        executor = Executors.newSingleThreadScheduledExecutor(threadFactory);
        executor.scheduleWithFixedDelay(this, 0, keepAliveTimeout, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        executor.shutdown();
    }

    @Override
    public void run() {
        for (Poller poller : pollers) {
            log.info("Cleaner 检测{} 所持有的Socket中...", poller.getPollerName());
            poller.cleanTimeoutSockets();
        }
        log.info("检测结束...");
    }
}
