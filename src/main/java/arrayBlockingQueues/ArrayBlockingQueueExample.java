package arrayBlockingQueues;

import java.util.concurrent.ArrayBlockingQueue;

public class ArrayBlockingQueueExample {

    public void takeAndPut() throws InterruptedException {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(5);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    queue.take();
                    System.out.println("线程：" + Thread.currentThread().getName() + "--> 第【" + i + "】数据取出");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "取出线程");
        t1.start();

        Thread.sleep(100);

        for (int i = 0; i < 10; i++) {
            queue.put("item_" + i);
            System.out.println("线程：" + Thread.currentThread().getName() + "--> 第【" + i + "】数据添加成功");
        }
    }

    public void addAndRemove(Integer maxSize) throws InterruptedException {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(maxSize);
        for (int i = 0; i < maxSize; i++) {
            queue.add("item_" + i);
            System.out.println("线程：" + Thread.currentThread().getName() + "--> 第【" + i + "】数据添加成功");
        }
        Thread.sleep(100);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < maxSize; i++) {
                queue.remove();
                System.out.println("线程：" + Thread.currentThread().getName() + "--> 第【" + i + "】数据取出");
            }
        }, "取出线程");
        t1.start();
    }

    public void lockInterrupted() throws InterruptedException {
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1);
        //先让生产者添加一个元素到队列当中
        Thread t1 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "开始执行");
                queue.put(1);
                System.out.println("线程:" + Thread.currentThread().getName() + "--> 完成添加");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        },"生产者");

        //创建消费者消费元素
        Thread t2 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "开始执行");
                queue.take();
                System.out.println("线程:" + Thread.currentThread().getName() + "--> 完成消费---1");

                queue.take();
                System.out.println("线程:" + Thread.currentThread().getName() + "--> 完成消费---2");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        },"消费者_01");



        t1.start();
        Thread.sleep(1000);
        t2.start();

        Thread.sleep(10000);
        t2.interrupt();

    }
}
