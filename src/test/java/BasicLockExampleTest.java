import org.junit.Test;
import reentrantLocks.BasicLockExample;

public class BasicLockExampleTest {

    @Test
    public void basicLockExampleTest() {
        BasicLockExample example = new BasicLockExample();

        // 创建10个线程并发执行
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    example.increment();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

}
