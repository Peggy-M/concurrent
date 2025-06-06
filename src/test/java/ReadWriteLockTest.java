import org.junit.Test;
import readWrite.ReentrantReadWriteLockExample;

public class ReadWriteLockTest {

    /**
     * 从案例可以看到的是,对于读锁而言是可以并发执行的,但是对于写锁一次只能有一个线程持有该锁,并且由上一个线程释放锁之后下一个线程才能执行
     */
    @Test
    public void testReentrantReadWriteLockExample() throws InterruptedException {
        final ReentrantReadWriteLockExample readWriteLock = new ReentrantReadWriteLockExample();

        //由于写锁先执行,所以对于读锁而言就是属于是互斥锁，处于等待阶段，等待写锁执行完毕才可以进行读
        for (int i = 0; i < 10; i++) {
            final int value = i;
            Thread thread = new Thread(() -> {
                readWriteLock.writeData(value);
            }, "Write Thread-" + i);
            thread.start();
        }


        for (int i = 0; i < 10; i++) {
            //由于读锁是一个共享锁,所以其他的线程是可以并发执行的...
            Thread thread = new Thread(readWriteLock::readData, "Read Thread-" + i);
            thread.start();
        }

        Thread.sleep(10000);
    }
}
