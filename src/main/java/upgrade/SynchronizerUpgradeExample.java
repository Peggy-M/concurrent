package upgrade;

import org.openjdk.jol.info.ClassLayout;

public class SynchronizerUpgradeExample {

    public void upgrade(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Object o = new Object();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        new Thread(() -> {

            synchronized (o){
                //t1  - 偏向锁
                System.out.println("t1:" + ClassLayout.parseInstance(o).toPrintable());
            }
        }).start();
        //main - 偏向锁 - 轻量级锁CAS - 重量级锁
        synchronized (o){
            System.out.println("main:" + ClassLayout.parseInstance(o).toPrintable());
        }
    }
}
