package threadPool;

import java.util.concurrent.RecursiveTask;

// 分段计算平方和的任务
class SumTask extends RecursiveTask<Long> {
    private final Integer[] array;
    private final int start;
    private final int end;
    private static final int THRESHOLD = 10_000; // 阈值

    SumTask(Integer[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD) {
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += (long) array[i] * array[i];
            }
            return sum;
        } else {
            int mid = (start + end) / 2;
            SumTask left = new SumTask(array, start, mid);
            SumTask right = new SumTask(array, mid, end);
            left.fork(); // 异步执行左半部分
            return right.compute() + left.join(); // 合并结果
        }
    }
}