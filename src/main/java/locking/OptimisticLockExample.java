package locking;

import java.util.concurrent.atomic.AtomicInteger;

public class OptimisticLockExample {
    private AtomicInteger balance = new AtomicInteger(100); // 使用原子类
    
    // 取款操作（乐观锁实现）
    public void withdraw(int amount) {
        // 自旋操作
        while (true) {
            //所有的线程都会到这里不断的执行....
            int current = balance.get();
            if (current < amount) {
                System.out.println(Thread.currentThread().getName() + " 余额不足");
                return;
            }
            // CAS操作：比较并交换
            if (balance.compareAndSet(current, current - amount)) {
                System.out.println(Thread.currentThread().getName() + " 取款成功，余额: " + balance.get());
                return;
            }
            // CAS失败，重试
            System.out.println(Thread.currentThread().getName() + " 操作冲突，重试...");
        }
    }
}