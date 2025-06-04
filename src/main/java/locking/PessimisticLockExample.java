package locking;

public class PessimisticLockExample {
    private int balance = 100; // 账户初始余额
    
    // 取款操作（悲观锁实现）
    public synchronized void withdraw(int amount) {
        if (balance >= amount) {
            try {
                // 模拟业务处理耗时
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            balance -= amount;
            System.out.println(Thread.currentThread().getName() + " 取款成功，余额: " + balance);
        } else {
            System.out.println(Thread.currentThread().getName() + " 余额不足");
        }
    }
}