package readWrite;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁案例测试
 */
public class ReentrantReadWriteLockExample {

    //创建一个读写锁
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    //获取读锁
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    //获取一个写锁
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    //定义一个共享资源
    private int shareData = 0;

    //读操作
    public  void readData() {
        //获取锁资源
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " read data:" + shareData);
            //模拟读取数据耗时...
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放所资源
            readLock.unlock();
            System.out.println(Thread.currentThread().getName() + "读锁释放锁资源.");
        }
    }

    //写操作
    public void writeData(int data) {
        //获取到写锁
        writeLock.lock();
        try {
            shareData = data;
            System.out.println(Thread.currentThread().getName() + " write data:" + shareData);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } finally {
            //同样的释放锁资源
            writeLock.unlock();
            System.out.println(Thread.currentThread().getName() + "写锁资源释放.");
        }

    }
}
