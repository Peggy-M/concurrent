package threadPool;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义一个线程池对象
 */

public class MyThreadPoolExecutor {
    private static volatile ThreadPoolExecutor executor;

    public static ThreadPoolExecutor create() {
        if (executor == null) {
            synchronized (MyThreadPoolExecutor.class) {
                final int corePoolSize = Runtime.getRuntime().availableProcessors(); //当前 Java 虚拟机可用的处理器数
                final int maximumPoolSize = corePoolSize * 2; //最大线程数
                final int keepAliveTime = 60; //阻塞队列等待的时间
                final TimeUnit unit = TimeUnit.SECONDS; //等待的时间单位
                final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize); //阻塞的队列
                final RejectedExecutionHandler handler = new MyThreadPoolExecutor.MyRejected();
                final ThreadFactory threadFactory = new ThreadFactory() {
                    private final AtomicInteger threadNumber = new AtomicInteger(1);

                    public Thread newThread(Runnable r) {
                        return new Thread(r, "MyThreadPoolExecutor-" + threadNumber.getAndIncrement());
                    }
                };
                if (executor == null) {
                    executor = new ThreadPoolExecutor(
                            corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler
                    );
                }
            }
        }
        return executor;
    }

    static class MyRejected implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println("执行了自定义的拒绝策略");
            executor.getQueue().poll();
            executor.execute(r);
        }
    }
}
