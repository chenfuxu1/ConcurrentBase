package htd.sharedmodeltubeside.four_thread_safe;

import java.util.ArrayList;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-22 11:56
 *
 * 线程安全分析
 **/
public class TestThreadSafe {
    private static final int THREAD_NUMBER = 2;
    private static final int LOOP_NUMBER = 200;

    /**
     * 其中一种情况是，如果线程 2 还未 add，线程 1 remove 就会报错
     * Exception in thread "Thread1" Exception in thread "Thread2" java.lang.ArrayIndexOutOfBoundsException: -1
     * 	at java.util.ArrayList.remove(ArrayList.java:507)
     * 	at htd.sharedmodeltubeside.four_thread_safe.ThreadUnsafe.method3(TestThreadSafe.java:39)
     * 	at htd.sharedmodeltubeside.four_thread_safe.ThreadUnsafe.method1(TestThreadSafe.java:34)
     * 	at htd.sharedmodeltubeside.four_thread_safe.TestThreadSafe.lambda$main$0(TestThreadSafe.java:20)
     * 	at java.lang.Thread.run(Thread.java:748)
     * java.lang.ArrayIndexOutOfBoundsException: -1
     * 	at java.util.ArrayList.add(ArrayList.java:465)
     * 	at htd.sharedmodeltubeside.four_thread_safe.ThreadUnsafe.method2(TestThreadSafe.java:43)
     * 	at htd.sharedmodeltubeside.four_thread_safe.ThreadUnsafe.method1(TestThreadSafe.java:32)
     * 	at htd.sharedmodeltubeside.four_thread_safe.TestThreadSafe.lambda$main$0(TestThreadSafe.java:20)
     * 	at java.lang.Thread.run(Thread.java:748)
     *
     */
    public static void main(String[] args) {
        ThreadUnsafe threadUnsafe = new ThreadUnsafe();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> {
                threadUnsafe.method1(LOOP_NUMBER);
            }, "Thread" + (i + 1)).start();
        }
    }
}

class ThreadUnsafe {
    ArrayList<String> mList = new ArrayList<>();

    public void method1(int loopNumber) {
        for (int i = 0; i < loopNumber; i++) {
            // 临界区，会产生竟态条件
            method2();

            method3();
        }
    }

    private void method3() {
        mList.remove(0);
    }

    private void method2() {
        mList.add("1");

    }
}
