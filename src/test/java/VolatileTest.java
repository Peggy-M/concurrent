import org.junit.Test;
import volatiles.Singleton;
import volatiles.VolatileExample;

import java.util.ArrayList;
import java.util.List;

public class VolatileTest {


    @Test
    public void testVolatileExample() throws InterruptedException {
        VolatileExample account = new VolatileExample();
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
    public void testVolatileSinglen() throws InterruptedException {
        VolatileExample account = new VolatileExample();
        List<Thread> threads = new ArrayList<>();

        // 创建5个线程同时取款
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() ->System.out.println(Singleton.getInstance()==null));
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }
}
