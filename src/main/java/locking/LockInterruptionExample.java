package locking;

import java.util.concurrent.locks.ReentrantLock;

public class LockInterruptionExample {
    private static final ReentrantLock lock = new ReentrantLock();

    public void lockInterrupted() throws InterruptedException {
        // 线程1先获取锁并持有较长时间
        Thread thread1 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Thread-1 获取了锁，将持有5秒");
                Thread.sleep(5000); // 模拟长时间持有锁
            } catch (InterruptedException e) {
                System.out.println("Thread-1 被中断");
            } finally {
                lock.unlock();
                System.out.println("Thread-1 释放了锁");
            }
        });


        // 线程2尝试获取锁，使用lockInterruptibly()
        Thread thread2 = new Thread(() -> {
            try {
                System.out.println("Thread-2 尝试获取锁(可中断方式)");
                lock.lockInterruptibly(); // 可中断的获取锁
                try {
                    System.out.println("Thread-2 成功获取了锁");
                } finally {
                    lock.unlock();
                    System.out.println("Thread-2 释放了锁");
                }
            } catch (InterruptedException e) {
                System.out.println("Thread-2 在等待锁时被中断");
            }
        });

        thread1.start();
        Thread.sleep(100); // 确保thread1先获取锁
        thread2.start();

        // 主线程等待1秒后中断thread2
        Thread.sleep(1000);
        System.out.println("主线程中断Thread-2");
        thread2.interrupt();

        thread1.join();
        thread2.join();
    }
}