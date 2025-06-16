import join.CriticalPoints;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

public class CriticalPointsTest {

    @Test
    public void joinsTest() {
        CriticalPoints criticalPoints = new CriticalPoints();
        try {
            System.out.println("主线程开始执行");
            criticalPoints.joinsExample();
            System.out.println("主线程执行结束");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void cyclicBarrierTest() {
        CriticalPoints criticalPoints = new CriticalPoints();
        criticalPoints.cyclicBarrierExample();

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void countDownLatchTest() throws InterruptedException {
        CriticalPoints criticalPoints = new CriticalPoints();
        criticalPoints.countDownLatchExample();
    }

    @Test
    public void futureTaskTest() throws ExecutionException, InterruptedException {
        CriticalPoints criticalPoints = new CriticalPoints();
        criticalPoints.futureTaskExample();
    }

    @Test
    public void completableFutureTest() {
        CriticalPoints criticalPoints = new CriticalPoints();
        criticalPoints.completableFutureExample();
    }

    @Test
    public  void serviceInitializerTest() throws InterruptedException {
        CriticalPoints criticalPoints = new CriticalPoints();
        criticalPoints.serviceInitializer();
    }

}
