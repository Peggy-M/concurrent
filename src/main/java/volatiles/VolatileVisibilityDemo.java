package volatiles;

public class VolatileVisibilityDemo {

    // 共享变量，测试时不加 volatile 和加 volatile 的区别
    private static volatile boolean running = true;

    public static void main(String[] args) throws InterruptedException {
        // 线程1：读取 running 变量
        Thread readerThread = new Thread(() -> {
            while (running) { // 如果 running 不是 volatile，可能看不到主线程的修改！
                // 空循环，模拟任务
            }
            System.out.println("ReaderThread: running 已变为 false，退出循环");
        });

        // 线程2：修改 running 变量
        Thread writerThread = new Thread(() -> {
            try {
                Thread.sleep(1000); // 等待1秒，确保 readerThread 已经启动
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            running = false; // 修改 running
            System.out.println("WriterThread: 已设置 running = false");
        });

        readerThread.start();
        writerThread.start();

        readerThread.join();
        writerThread.join();
    }
}