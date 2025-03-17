package htd.javathread.three_thread_methods;


import htd.utils.Sout;

import java.util.concurrent.locks.LockSupport;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-16 11:29
 * <p>
 * Desc:
 */
public class TestInterrupt {
    private static final String TAG = "TestInterrupt";

    public static void main(String[] args) throws Exception {
        // test1();
        // test2();
        test3();
    }

    /**
     * 打断正在睡眠的线程
     * java.lang.InterruptedException: sleep interrupted
     * at java.lang.Thread.sleep(Native Method)
     * at htd.javathread.three_thread_methods.TestInterrupt.lambda$test1$0(TestInterrupt.java:23)
     * at java.lang.Thread.run(Thread.java:748)
     * <p>
     * 2025-03-16 11:31:48.579	main	TestInterrupt	 打断状态 t1.isInterrupted(): false
     * 调用 interrupt 后，isInterrupted 是为 true，但是当处于 sleep 时，会抛出 InterruptedException 异常
     * 这时又会将打断标记置为 false
     * 所以，最终输出的是 false
     */
    private static void test1() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        t1.start();
        Thread.sleep(500);
        t1.interrupt();
        Sout.d(TAG, " 打断状态 t1.isInterrupted(): " + t1.isInterrupted());
    }

    /**
     * 2025-03-16 11:36:30.210	t2	TestInterrupt	 打断状态 interrupted: true
     * 打断正在运行的线程
     * 只是会获取到 current.isInterrupted() 当前的标记是 true，不会强行打断
     */
    private static void test2() throws InterruptedException {
        Thread t2 = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                boolean interrupted = current.isInterrupted();
                if (interrupted) {
                    Sout.d(TAG, " 打断状态 interrupted: " + interrupted);
                    break;
                }
            }
        }, "t2");
        t2.start();
        Thread.sleep(500);
        t2.interrupt();
    }

    /**
     * 打断 park 线程, 不会清空打断状态
     * 2025-03-16 14:23:38.364	t1		TestInterrupt	park...
     * 2025-03-16 14:23:38.856	t1		TestInterrupt	unpark...
     * 2025-03-16 14:23:38.856	t1		TestInterrupt	打断状态：true
     */
    private static void test3() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            Sout.d(TAG, "park...");
            LockSupport.park();
            Sout.d(TAG, "unpark... 1");
            Sout.d(TAG, "打断状态：" + Thread.currentThread().isInterrupted());

            // isInterrupted 是 true 的时候，再次 park 就会失效了
            LockSupport.park();
            Sout.d(TAG, "unpark...2");
        }, "t1");
        t1.start();
        Thread.sleep(500);
        t1.interrupt();
    }
}
