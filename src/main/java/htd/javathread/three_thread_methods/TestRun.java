package htd.javathread.three_thread_methods;

import htd.utils.Sout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-15 22:49
 * <p>
 * Desc:
 */
public class TestRun {
    private static final String TAG = "TestRun";

    public static void main(String[] args) {
        // test1();
        test2();
    }

    // 直接调 run 方法就相当于一个普通方法，并不会开启新的线程
    private static void test1() {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                Sout.d(TAG, "cfx running..."); // 2025-03-15 22:52:00.729	main	TestRun	cfx running...
            }
        };
        t1.run();
        Sout.d(TAG, "cfx do others things");
    }

    // start 才会启动新的线程
    private static void test2() {
        Thread t2 = new Thread("t2") {
            @Override
            public void run() {
                Sout.d(TAG, "cfx running..."); // 2025-03-15 22:53:28.879	t2		TestRun	cfx running...
            }
        };
        t2.start();
        Sout.d(TAG, "cfx do others things");
    }
}
