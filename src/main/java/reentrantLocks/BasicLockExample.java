package reentrantLocks;

import java.util.concurrent.locks.ReentrantLock;

public class BasicLockExample {
    private final ReentrantLock lock = new ReentrantLock();
    private int count = 0;

    public void increment() {
        lock.lock();  // 获取锁
        try {
            count++;
            System.out.println(Thread.currentThread().getName() + " count: " + count);
        } finally {
            lock.unlock();  // 确保锁被释放
        }
    }
}