package htd.javathread.four_daemon;

import htd.utils.Sout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-17 16:06
 * <p>
 * Desc:
 */
public class TestDaemon {
    private static final String TAG = "TestDaemon";

    public static void main(String[] args) {
        test1();
    }

    /**
     * 2025-03-17 16:08:49.152	main		TestDaemon	开始运行...
     * 2025-03-17 16:08:49.174	daemon		TestDaemon	t1 开始运行...
     * 2025-03-17 16:08:50.175	main		TestDaemon	运行结束...
     */
    private static void test1() {
        Sout.d(TAG, "开始运行...");
        Thread t1 = new Thread(() -> {
            Sout.d(TAG, "t1 开始运行...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Sout.d(TAG, "t1 运行结束...");
        }, "daemon");

        // 设置该线程为守护线程
        t1.setDaemon(true);
        t1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Sout.d(TAG, "运行结束...");
    }
}
