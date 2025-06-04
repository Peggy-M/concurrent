
import locking.OptimisticLockExample;
import locking.PessimisticLockExample;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class LockTest {

    @Test
    public void OptimisticLockExampleTest() throws InterruptedException {
        OptimisticLockExample account = new OptimisticLockExample();
        List<Thread> threads = new ArrayList<>();

        // 创建5个线程同时取款
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> account.withdraw(30), "线程" + i);
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }


    @Test
    public void PessimisticLockExampleTest() throws InterruptedException {
        PessimisticLockExample account = new PessimisticLockExample();
        List<Thread> threads = new ArrayList<>();
        // 创建5个线程同时取款
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> account.withdraw(30), "线程" + i);
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }

    }

}
