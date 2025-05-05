package htd.sharedmodeltool.thread_pool.thread_pool_executor;

import htd.utils.Sout;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-05-03 11:36
 **/
public class TestTimer {
    private static final String TAG = "TestTimer";

    public static void main(String[] args) {
        // test1();
        // test2();
        // test3();
        // test4();
        test5();
    }

    /**
     * 缺点：如果发生异常，整个线程池都不会执行了
     * 2025-05-03 15:37:23.727	Timer-0		TestTimer	task 1
     * 2025-05-03 15:37:25.729	Timer-0		TestTimer	task 2
     */
    private static void test1() {
        Timer timer = new Timer();
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                Sout.d(TAG, "task 1");
                // int a = 1 / 0;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                Sout.d(TAG, "task 2");
            }
        };
        // 使用 timer 添加两个任务，希望它们都在 1s 后执行
        // 但由于 timer 内只有一个线程来顺序执行队列中的任务，因此『任务1』的延时，影响了『任务2』的执行
        timer.schedule(task1, 1000);
        timer.schedule(task2, 1000);
    }

    /**
     * 一个任务失败不会影响到其他线程
     * 2025-05-03 16:50:25.182	pool-1-thread-2		TestTimer	任务 2，执行时间：Sat May 03 16:50:25 CST 2025
     * 2025-05-03 16:50:25.182	pool-1-thread-1		TestTimer	任务 1，执行时间：Sat May 03 16:50:25 CST 2025
     */
    private static void test2() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        // 添加两个任务，希望它们都在 2s 后执行
        executor.schedule(() -> {
            Sout.d(TAG, "任务 1，执行时间：" + new Date());
            int a = 1 / 0;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        }, 1000, TimeUnit.MILLISECONDS);

        executor.schedule(() -> {
            Sout.d(TAG, "任务 2，执行时间：" + new Date());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        }, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * 2025-05-03 16:58:26.251	main		TestTimer	start...
     * 2025-05-03 16:58:27.285	pool-1-thread-1		TestTimer	running...
     * 2025-05-03 16:58:28.294	pool-1-thread-1		TestTimer	running...
     * 2025-05-03 16:58:29.290	pool-1-thread-1		TestTimer	running...
     * 2025-05-03 16:58:30.297	pool-1-thread-1		TestTimer	running...
     */
    private static void test3() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Sout.d(TAG, "start...");
        executor.scheduleAtFixedRate(() -> {
            Sout.d(TAG, "running...");
        }, 1, 1, TimeUnit.SECONDS);
    }

    /**
     * 任务执行时间超过了间隔时间
     * 输出分析：一开始，延时 1s，接下来，由于任务执行时间 > 间隔时间，间隔被『撑』到了 2s
     * 2025-05-03 17:03:07.829	main		TestTimer	start...
     * 2025-05-03 17:03:08.864	pool-1-thread-1		TestTimer	running...
     * 2025-05-03 17:03:10.868	pool-1-thread-1		TestTimer	running end...
     * 2025-05-03 17:03:10.868	pool-1-thread-1		TestTimer	running...
     * 2025-05-03 17:03:12.871	pool-1-thread-1		TestTimer	running end...
     */
    private static void test4() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Sout.d(TAG, "start...");
        executor.scheduleAtFixedRate(() -> {
            Sout.d(TAG, "running...");
            try {
                Thread.sleep(2000);
                Sout.d(TAG, "running end...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    /**
     * 输出分析：一开始，延时 1s，scheduleWithFixedDelay 的间隔是 上一个任务结束 <-> 延时 <-> 下一个任务开始 所
     * 以间隔都是 3s
     * 整个线程池表现为：线程数固定，任务数多于线程数时，会放入无界队列排队。任务执行完毕，这些线
     * 程也不会被释放。用来执行延迟或反复执行的任务
     * 2025-05-03 17:05:16.592	main		TestTimer	start...
     * 2025-05-03 17:05:17.630	pool-1-thread-1		TestTimer	running...
     * 2025-05-03 17:05:19.646	pool-1-thread-1		TestTimer	running end...
     * 2025-05-03 17:05:20.658	pool-1-thread-1		TestTimer	running...
     * 2025-05-03 17:05:22.660	pool-1-thread-1		TestTimer	running end...
     * 2025-05-03 17:05:23.666	pool-1-thread-1		TestTimer	running...
     * 2025-05-03 17:05:25.669	pool-1-thread-1		TestTimer	running end...
     */
    private static void test5() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Sout.d(TAG, "start...");
        executor.scheduleWithFixedDelay(() -> {
            Sout.d(TAG, "running...");
            try {
                Thread.sleep(2000);
                Sout.d(TAG, "running end...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

}
