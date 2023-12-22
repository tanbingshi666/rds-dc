package com.skysec.soc.rds.dc.utils;

import lombok.Getter;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Future工具类
 *
 * @param <T>
 */
public class FutureUtil<T> {

    private static final String EXECUTOR_THREAD_POOL_NAME = "future-executor";
    private static final String SCHEDULED_EXECUTOR_THREAD_POOL_NAME = "future-scheduled-executor";

    @Getter
    private ThreadPoolExecutor executor;
    private ScheduledThreadPoolExecutor scheduledExecutor;

    public static final FutureUtil<Void> executorFuture = FutureUtil.init(8, 8, 10240);

    private FutureUtil() {
    }

    public static <T> FutureUtil<T> init(int corePoolSize, int maxPoolSize, int queueSize) {
        FutureUtil<T> futureUtil = new FutureUtil<>();

        futureUtil.executor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                300,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(queueSize),
                new NamedThreadFactory(EXECUTOR_THREAD_POOL_NAME),
                new ThreadPoolExecutor.DiscardOldestPolicy() //对拒绝任务不抛弃，而是抛弃队列里面等待最久的一个线程，然后把拒绝任务加到队列。
        );
        futureUtil.executor.allowCoreThreadTimeOut(true);

        futureUtil.scheduledExecutor = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory(SCHEDULED_EXECUTOR_THREAD_POOL_NAME));

        return futureUtil;
    }

    public FutureUtil<T> submitTask(Runnable runnable) {
        executor.submit(runnable);
        return this;
    }

    public FutureUtil<T> scheduleTask(Runnable runnable,
                                      long initialDelay,
                                      long period,
                                      TimeUnit unit) {
        scheduledExecutor.scheduleAtFixedRate(
                runnable,
                initialDelay,
                period,
                unit
        );
        return this;
    }
}
