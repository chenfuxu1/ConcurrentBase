package htd.sharedmodelmemory.one_visible;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-29 13:35
 * <p>
 * Desc:
 * 可见性
 * 退不出的循环
 * 先来看一个现象，main 线程对 run 变量的修改对于 t 线程不可见，导致了 t 线程无法停止
 **/
public class TestNotBreak {
    private static boolean sRun = true;

    /**
     * 没有退出的原因？
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
