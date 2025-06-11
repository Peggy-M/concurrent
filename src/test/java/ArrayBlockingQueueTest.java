import arrayBlockingQueues.ArrayBlockingQueueExample;
import org.junit.Test;

public class ArrayBlockingQueueTest {

    @Test
    public void takeAndPutTest() throws InterruptedException {
        ArrayBlockingQueueExample queueExample = new ArrayBlockingQueueExample();
        queueExample.takeAndPut();
    }

    @Test
    public void addAndRemoveTest() throws InterruptedException {
        ArrayBlockingQueueExample queueExample = new ArrayBlockingQueueExample();
        queueExample.addAndRemove(10);
    }

    @Test
    public void lockInterrupted() throws InterruptedException {
        ArrayBlockingQueueExample queueExample = new ArrayBlockingQueueExample();
        queueExample.lockInterrupted();
    }
}
