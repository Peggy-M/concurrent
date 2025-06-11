package threadPool;

import java.util.Date;
import java.util.concurrent.*;

public class ThreadPoolExecutorExample {

    public static void printf() {
        System.out.println(Thread.currentThread().getName() + "开始执行");
    }

    public static void printf1() {
        System.out.println(Thread.currentThread().getName() + "==> ==> t1 开始执行");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName() + "==> t1 执行结束");
    }

    public static void printf2() {
        System.out.println(Thread.currentThread().getName() + "==> t2 开始执行");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName() + "==> t2 执行结束");
    }

    public static void printf3() {
        System.out.println(Thread.currentThread().getName() + "==> t3 开始执行");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName() + "==> t3 执行结束");
    }

    public static void delayTask(Integer name, Integer delay) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName() + "==>" + name + "延时[" + delay + "]秒" + "====" + new Date());
    }


    public void fixedThreadPool() {
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        executorService.execute(ThreadPoolExecutorExample::printf1);
        executorService.execute(ThreadPoolExecutorExample::printf2);
        executorService.execute(ThreadPoolExecutorExample::printf3);
    }

    public void fixedThreadPoolMax(Boolean isShutDown) {
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        for (int i = 0; i < 200; i++) {
            executorService.execute(ThreadPoolExecutorExample::printf);
        }
        //这里如果不执行 shutdown 就不会被回收
        if (isShutDown) executorService.shutdown();
    }


    public void singleThreadExecutor() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(ThreadPoolExecutorExample::printf1);
        executorService.execute(ThreadPoolExecutorExample::printf2);
        executorService.execute(ThreadPoolExecutorExample::printf3);

    }

    //当第一次提交任务到线程池时,会直接构建一个工程线程,这个工作线程在执行完后, 60 秒没有任务可以执行后,会结束，如果等待 60 秒内有任务会再次拿到这个任务再执行
    public void cachedThread() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(ThreadPoolExecutorExample::printf1);
        executorService.execute(ThreadPoolExecutorExample::printf2);
        executorService.execute(ThreadPoolExecutorExample::printf3);
    }

    //定期执行 or 延时执行-使用[延时队列]
    public void scheduledThread() {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        System.out.println("开始执行的时间:--> " + new Date());
        scheduledThreadPool.schedule(() -> {
            ThreadPoolExecutorExample.delayTask(1, 1);
        }, 1, TimeUnit.SECONDS);

        scheduledThreadPool.schedule(() -> {
            ThreadPoolExecutorExample.delayTask(2, 5);
        }, 5, TimeUnit.SECONDS);

        scheduledThreadPool.schedule(() -> {
            ThreadPoolExecutorExample.delayTask(3, 10);
        }, 10, TimeUnit.SECONDS);
        System.out.println("执行结束的时间:--> " + new Date());

        scheduledThreadPool.scheduleAtFixedRate(() -> {
            ThreadPoolExecutorExample.delayTask(4, 3);
        }, 3, 5, TimeUnit.SECONDS);
        System.out.println("执行结束的时间:--> " + new Date());
    }

    //工作窃取线程
    public void workStealingThread() {
        Integer[] maxArray = new Integer[100_000_000];
        System.out.println("开始时间:--> " + new Date());
        for (int i = 0; i < maxArray.length; i++) {
            maxArray[i] = i * 1000;
        }
        System.out.println("结束时间:--> " + new Date());

        ForkJoinPool forkJoinPool = new ForkJoinPool(); // 工作窃取池的底层实现
        System.out.println("============================================================================");
        System.out.println("开始时间:--> " + new Date());
        long result = forkJoinPool.invoke(new SumTask(maxArray, 0, maxArray.length));
        System.out.println("结束时间:--> " + new Date());


    }

}
