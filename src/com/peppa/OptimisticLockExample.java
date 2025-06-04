package com.peppa;

import java.util.concurrent.atomic.AtomicInteger;

public class OptimisticLockExample {
    private AtomicInteger balance = new AtomicInteger(100); // 使用原子类
    
    // 取款操作（乐观锁实现）
    public void withdraw(int amount) {
        // 自旋操作
        while (true) {
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

    public static void main(String[] args) {
        OptimisticLockExample account = new OptimisticLockExample();
        
        // 创建5个线程同时取款
        for (int i = 0; i < 5; i++) {
            new Thread(() -> account.withdraw(30), "线程" + i).start();
        }
    }
}