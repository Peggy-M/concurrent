import org.junit.Test;
import synchronizeds.SynchronizedExample;

import java.util.ArrayList;
import java.util.List;

public class SynchronizedTest {

    @Test
    public void incrementTest() throws InterruptedException {
        SynchronizedExample example = new SynchronizedExample();
        example.increment();

        List<Thread> threads = new ArrayList<>();

        // 创建5个线程同时取款
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                example.increment();
                System.out.println(Thread.currentThread().getName() + ": increment=" + example.getCount());
            });
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        Thread.sleep(5000);
    }

    @Test
    public void increment()   {
        SynchronizedExample accountA = new SynchronizedExample(1000);
        SynchronizedExample accountB = new SynchronizedExample(1000);
        // 线程1：A 向 B 转账 500
        Thread t1 = new Thread(() -> accountA.transfer(accountB, 500));
        // 线程2：B 向 A 转账 200
        Thread t2 = new Thread(() -> accountB.transfer(accountA, 200));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("A 余额: " + accountA.getBalance()); // 正确输出 700
        System.out.println("B 余额: " + accountB.getBalance()); // 正确输出 1300
    }
}
