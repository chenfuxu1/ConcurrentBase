package htd.javathread.three_thread_methods;

import htd.utils.Sout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-16 10:50
 * <p>
 * Desc:
 */
public class TestJoin {
    private static final String TAG = "TestJoin";
    private static int result = 0;
    private static int r1 = 0;
    private static int r2 = 0;

    public static void main(String[] args) {
        // test1();
        // test2();
        test3();
    }

    /**
     * join 会等待线程执行完成，才会往下执行
     * 2025-03-16 10:55:00.440	main	TestJoin	开始
     * 2025-03-16 10:55:00.462	t1	TestJoin	开始
     * 2025-03-16 10:55:00.464	t1	TestJoin	结束
     * 2025-03-16 10:55:00.464	main TestJoin	结果为 result: 10
     */
    private static void test1() {
        Sout.d(TAG, "开始");
        Thread t1 = new Thread(() -> {
            Sout.d(TAG, "开始");
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Sout.d(TAG, "结束");
            result = 10;
        }, "t1");
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Sout.d(TAG, "结果为 result: " + result);
    }

    /**
     * 等待两个线程的执行结果
     * 2025-03-16 11:05:13.521	main TestJoin	 r1: 10 r2: 20 time: 2002 ms
     */
    private static void test2() {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                r1 = 10;
            }
        };

        Thread t2 = new Thread("t3") {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                r2 = 20;
            }
        };

        long start = System.currentTimeMillis();
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        Sout.d(TAG, " r1: " + r1 + " r2: " + r2 + " time: " + (end -start) + " ms");
    }

    /**
     * 有时效的 join
     * 2025-03-16 11:10:15.146	main	TestJoin	 r1: 0 time: 1006 ms
     * 只等待了 1s，主线程就往下执行了，所以拿不到 r1 的值
     */
    private static void test3() {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                r1 = 10;
            }
        };

        long start = System.currentTimeMillis();
        t1.start();
        try {
            t1.join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        Sout.d(TAG, " r1: " + r1  + " time: " + (end -start) + " ms");
    }
}
