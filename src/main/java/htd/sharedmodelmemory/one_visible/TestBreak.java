package htd.sharedmodelmemory.one_visible;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-29 13:42
 * <p>
 * Desc:
 * 可见性
 * 退不出的循环
 * 解决方法
 */
public class TestBreak {
    private volatile static boolean sRun = true;

    /**
     * volatile（易变关键字）
     * 它可以用来修饰成员变量和静态成员变量，他可以避免线程从自己的工作缓存中查找变量的值，必须到主存中获取
     * 它的值，线程操作 volatile 变量都是直接操作主存
     */
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (sRun) {
                //
            }
        }, "t1");
        t1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sRun = false; // 线程 t1 不会如预想的停下来
    }
}
