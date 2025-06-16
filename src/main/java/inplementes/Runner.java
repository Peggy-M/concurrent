package inplementes;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class Runner implements Runnable {
    private final CyclicBarrier startingGate;
    private final String name;

    public Runner(CyclicBarrier startingGate, String name) {
        this.startingGate = startingGate;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            // 运动员准备中
            System.out.println(name + " 正在热身...");
            TimeUnit.MILLISECONDS.sleep((long)(Math.random() * 1000));
            
            System.out.println(name + " 已到达起跑线，等待其他选手...");
            
            // 等待所有运动员就位（屏障点）
            startingGate.await();
            
            // 比赛开始后执行
            System.out.println(name + " 起跑！");
            TimeUnit.MILLISECONDS.sleep((long)(Math.random() * 3000)); // 模拟跑步时间
            System.out.println(name + " 到达终点！");
            
        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.println(name + " 退出比赛：" + e.getMessage());
        }
    }
}