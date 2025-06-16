import threadPool.MyThreadPoolExecutor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class MyThreadPoolExecutorTest {
    public void testCreateThreadPool() throws ExecutionException, InterruptedException {

        ThreadPoolExecutor pool = MyThreadPoolExecutor.create();

        Future<String> submit = pool.submit(() -> {
            //执行有返回结果的任务
            return "返回的结果....";
        });
        String result = submit.get(); //获取执行的返回结果

        pool.execute(() -> {
            //执行没有返回结果任务
        });

        pool.shutdown();
    }
}
