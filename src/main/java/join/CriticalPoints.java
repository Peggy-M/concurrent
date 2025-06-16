package join;


import inplementes.Runner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

// 线程临界点测试
public class CriticalPoints {

    public void joinsExample() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            try {
                System.out.println("线程 1 正在阻塞");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                System.out.println("线程 2 正在阻塞");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }


    public void cyclicBarrierExample() {
        // 参赛运动员数量
        final int RUNNER_COUNT = 5;

        // 创建CyclicBarrier，指定参赛人数和比赛开始的信号（裁判开枪）
        CyclicBarrier startingGate = new CyclicBarrier(RUNNER_COUNT, () -> {
            System.out.println("\n所有运动员准备完毕！");
            System.out.println("砰！比赛开始！\n");
        });

        // 创建并启动运动员线程
        for (int i = 1; i <= RUNNER_COUNT; i++) {
            new Thread(new Runner(startingGate, "运动员-" + i)).start();
            try {
                // 模拟运动员陆续到达起跑线的时间差
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void countDownLatchExample() throws InterruptedException {
        int runnerCount = 4;
        CountDownLatch startSignal = new CountDownLatch(1); // 发令枪信号
        CountDownLatch finishSignal = new CountDownLatch(runnerCount); // 所有选手完成

        for (int i = 0; i < runnerCount; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " 等待起跑信号");
                    startSignal.await(); // 等待发令枪
                    System.out.println(Thread.currentThread().getName() + " 起跑");
                    Thread.sleep((long) (Math.random() * 3000)); // 模拟跑步时间
                    System.out.println(Thread.currentThread().getName() + " 到达终点");
                    finishSignal.countDown(); // 完成比赛
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "Runner-" + i).start();
        }

        Thread.sleep(1000); // 裁判准备
        System.out.println("发令枪响！");
        startSignal.countDown(); // 发令枪信号（计数器减 1，所有 Runner 线程开始执行）

        finishSignal.await(); // 等待所有选手完成
        System.out.println("比赛结束");
    }


    public void futureTaskExample() throws ExecutionException, InterruptedException {
        List<Callable<Integer>> callables = new ArrayList<>();
        int result = 0;
        for (int i = 0; i < 10; i++) {
            final int num = i;
            Callable<Integer> callable = () -> {
                System.out.println("子线程开始计算...");
                Thread.sleep(100); // 模拟耗时计算
                return num + 1; // 返回计算结果
            };
            callables.add(callable);
        }

        System.out.println("主线程可以做其他事情...");

        for (Callable<Integer> callable : callables) {
            FutureTask<Integer> futureTask = new FutureTask<>(callable);
            new Thread(futureTask).start(); // 启动线程执行任务
            result = result + futureTask.get(); // 阻塞，直到获取计算结果
        }

        System.out.println("计算结果: " + result);
    }


    public void completableFutureExample() {
        CompletableFuture.supplyAsync(() -> {
            // 第一个任务：获取用户ID
            System.out.println("任务1: 获取用户ID");
            return 123;
        }).thenApply(userId -> {
            // 第二个任务：根据用户ID获取用户名
            System.out.println("任务2: 根据用户ID(" + userId + ")获取用户名");
            return "User-" + userId;
        }).thenAccept(userName -> {
            // 第三个任务：打印用户名
            System.out.println("任务3: 用户名是 " + userName);
        });

        // 等待所有任务完成
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void serviceInitializer() throws InterruptedException {
        final int SERVICE_COUNT = 3;
        final CountDownLatch latch = new CountDownLatch(SERVICE_COUNT);

        // 模拟三个服务的初始化
        ExecutorService executor = Executors.newFixedThreadPool(SERVICE_COUNT);

        executor.execute(() -> {
            initDatabase();
            latch.countDown();
        });

        executor.execute(() -> {
            initCache();
            latch.countDown();
        });

        executor.execute(() -> {
            initMQ();
            latch.countDown();
        });

        // 主线程等待所有服务初始化完成
        latch.await(10, TimeUnit.SECONDS);
        System.out.println("所有服务初始化完成，开始接收请求");
        executor.shutdown();
    }



    private void initDatabase() {
        System.out.println("初始化检查算法是否已经启动====>");
        try {
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("算法已启动<====");
    }

    private void initCache() {
        System.out.println("初始化清除缓存数据====>");
        try {
            Thread.sleep((long) (Math.random() * 5000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("缓存数据初始化完成<====");
    }

    private void initMQ() {
        System.out.println("初始化 MQ 消息队列数据====>");
        try {
            Thread.sleep((long) (Math.random() * 10000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("初始化消息队列数据完成<====");
    }
}
