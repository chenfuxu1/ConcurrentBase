package htd.javathread.three_thread_methods;

import htd.utils.Sout;

import java.util.concurrent.TimeUnit;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-15 23:01
 * <p>
 * Desc:
 */
public class TestSleepAndYield {
    private static final String TAG = "TestSleepAndYield";

    public static void main(String[] args) {
        // test1();
        // test2();
        // test3();
        test4();
    }

    // 调用 sleep 会让当前线程从 Running 进入 Timed Waiting 状态（阻塞）
    private static void test1() {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        Sout.d(TAG, " t1 state: " + t1.getState()); // 2025-03-15 23:04:26.446	main	TestSleepAndYield	 t1 state:
        // RUNNABLE

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Sout.d(TAG, " t1 state: " + t1.getState()); // 2025-03-15 23:04:26.950	main	TestSleepAndYield	 t1 state:
        // TIMED_WAITING
    }

    /**
     * 睡眠的线程被唤醒，会抛出 InterruptedException 异常
     * 2025-03-15 23:09:27.558	t2		TestSleepAndYield	enter sleep...
     * 2025-03-15 23:09:28.549	main		TestSleepAndYield	interrupt...
     * 2025-03-15 23:09:28.549	t2		TestSleepAndYield	wake up...
     */
    private static void test2() {
        Thread t2 = new Thread("t2") {
            @Override
            public void run() {
                try {
                    Sout.d(TAG, "enter sleep...");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Sout.d(TAG, "wake up...");
                    e.printStackTrace();
                }
            }
        };
        t2.start();
        try {
            Thread.sleep(1000);
            // 1s 后强制唤醒
            Sout.d(TAG, "interrupt...");
            t2.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * TimeUnit.SECONDS.sleep 内部也是使用 Thread.sleep 阻塞线程
     * 2025-03-15 23:23:25.880	main	TestSleepAndYield	enter sleep
     * 2025-03-15 23:23:26.893	main	TestSleepAndYield	end sleep
     */
    private static void test3() {
        Sout.d(TAG, "enter sleep");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Sout.d(TAG, "end sleep");
    }

    /**
     * 线程优先级，默认是 5
     */
    private static void test4() {
        Runnable task1 = () -> {
            int count = 0;
            for (; ; ) {
                Sout.d(TAG, "---->task1 count: " + count++);
            }
        };
        Runnable task2 = () -> {
            int count = 0;
            for (; ; ) {
                // Thread.yield(); // 让出 t2 的执行权，让 t1 多执行，t1 的 count 就会加的更多
                Sout.d(TAG, "\t---->task2 count " + count++);
            }
        };
        Thread t1 = new Thread(task1, "t1");
        Thread t2 = new Thread(task2, "t2");

        // 这样 t2 会获得更多的执行权
        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.MAX_PRIORITY);

        t1.start();
        t2.start();
    }
}
