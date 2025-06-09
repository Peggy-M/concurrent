package synchronizeds;

public class SynchronizedExample {

    private final Object lock = new Object(); // 专用锁对象
    private int balance;
    private int count = 0;

    public SynchronizedExample(int initialBalance) {
        this.balance = initialBalance;
    }

    public SynchronizedExample( ) {
    }

    // 同步实例方法（锁是 this）
    public synchronized void increment() {
        count++;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {

            throw new RuntimeException(e);
        }
    }
    public int getCount() {
        return count;
    }


    // 转账方法（需同步）
    public void transfer(SynchronizedExample target, int amount) {
        synchronized (lock) { // 锁定当前账户
            synchronized (target.lock) { // 锁定目标账户
                if (this.balance >= amount) {
                    this.balance -= amount;
                    target.balance += amount;
                    System.out.println(Thread.currentThread().getName() + " 转账成功");
                } else {
                    System.out.println(Thread.currentThread().getName() + " 余额不足");
                }
            }
        }
    }

    public int getBalance() {
        return balance;
    }
}
