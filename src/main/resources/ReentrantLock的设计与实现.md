## ReentrantLock 的设计与实现

### AQS 是什么，它在 Lock 的显示锁当中起到什么作用？

AQS 的全文是 AbstractQueuedSynchronizer 其底层主要是通过一个双向链表维护了 Node 对象的节点，并通过 volatile 来修饰了一个全局的 state 锁状态值。在 Java 并发包下ReentrantLock`、`Semaphore`、`CountDownLatch`、`ReentrantReadWriteLock这些都是基于此抽象接口实现。

其主要的方法有

- **`tryAcquire(int)`**：尝试获取锁（独占模式）
- **`tryRelease(int)`**：尝试释放锁（独占模式）
- **`tryAcquireShared(int)`**：尝试获取共享锁（共享模式）
- **`tryReleaseShared(int)`**：尝试释放共享锁（共享模式）

### ReentrantLock 的底层实现

我们在将 ReentrantLock  之前我们先从一个案例开始

~~~ java
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
~~~

~~~ java
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
~~~

·
