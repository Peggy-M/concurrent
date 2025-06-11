package linkedBlockingQueues;

import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingQueueExample {

    public void lockInterrupted(){
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
        queue.offer("a");
    }
}
