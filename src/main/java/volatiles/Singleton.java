package volatiles;

public class Singleton {
    private static /* volatile */ Singleton instance; // 测试时去掉 volatile

    private int value; // 用于检查对象是否初始化

    private Singleton() {
        // 模拟初始化耗时操作，增加重排序概率
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.value = 42; // 正确初始化后 value 应该是 42
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton(); // 可能发生指令重排序
                }
            }
        }
        return instance;
    }

    public int getValue() {
        return value;
    }
}