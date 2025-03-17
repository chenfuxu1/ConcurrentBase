package htd.javathread.two_thread_theory;

import htd.utils.Sout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-13 0:01
 * <p>
 * Desc:
 */
public class Frames2 {
    private static final String TAG = "Frames2";

    public static void main(String[] args) {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                method1(20);
            }
        };
        t1.setName("t1");
        t1.start();

        method1(10);
    }

    private static void method1(int x) {
        int y = x + 1;
        Object m = method2();
        Sout.d(TAG, m.toString());
    }

    private static Object method2() {
        Object n = new Object();
        return n;
    }
}
