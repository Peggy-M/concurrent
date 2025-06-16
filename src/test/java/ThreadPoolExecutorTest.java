import org.junit.Test;
import threadPool.ThreadPoolExecutorExample;

public class ThreadPoolExecutorTest {


    @Test
    public void executorExampleTest(){
        ThreadPoolExecutorExample example = new ThreadPoolExecutorExample();
        example.fixedThreadPool();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void executorExampleMaxTest(){
        ThreadPoolExecutorExample example = new ThreadPoolExecutorExample();
        example.fixedThreadPoolMax(false);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void singleThreadTest(){
        ThreadPoolExecutorExample example = new ThreadPoolExecutorExample();
        example.singleThreadExecutor();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void cachedThreadPoolTest(){
        ThreadPoolExecutorExample example = new ThreadPoolExecutorExample();
        example.cachedThread();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void scheduledThreadTest(){
        ThreadPoolExecutorExample example = new ThreadPoolExecutorExample();
        example.scheduledThread();
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void fixedThreadPoolTest(){
        ThreadPoolExecutorExample example = new ThreadPoolExecutorExample();
        example.workStealingThread();
        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
