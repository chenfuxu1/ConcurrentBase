package htd.sharedmodeltool.thread_pool.thread_pool_executor;

import htd.utils.Sout;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-05-01 9:46
 **/
public class TestShutDown {
    private static final String TAG = "TestShutDown";

    public static void main(String[] args) throws Exception {
        // shutdown();

        // shutdownWait();

        shutdownNow();
    }

    /**
     * 线程池状态变为 SHUTDOWN
     * 不会接收新任务
     * 但已提交任务会执行完
     * 此方法不会阻塞调用线程的执行
     * 2025-05-01 10:11:23.182	pool-1-thread-2		TestShutDown	shutdown task 2 running...
     * 2025-05-01 10:11:23.182	pool-1-thread-1		TestShutDown	shutdown task 1 running...
     * 2025-05-01 10:14:29.078	main		TestShutDown	不会阻塞
     * 2025-05-01 10:11:24.187	pool-1-thread-2		TestShutDown	shutdown task 2 finish...
     * 2025-05-01 10:11:24.187	pool-1-thread-1		TestShutDown	shutdown task 1 finish...
     * 2025-05-01 10:11:24.187	pool-1-thread-1		TestShutDown	shutdown task 3 running...
     * 2025-05-01 10:11:25.191	pool-1-thread-1		TestShutDown	shutdown task 3 finish...
     */
    public static void shutdown() {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<Integer> result1 = pool.submit(() -> {
            Sout.d(TAG, "shutdown task 1 running...");
            Thread.sleep(1000);
            Sout.d(TAG, "shutdown task 1 finish...");
            return 1;
        });

        Future<Integer> result2 = pool.submit(() -> {
            Sout.d(TAG, "shutdown task 2 running...");
            Thread.sleep(1000);
            Sout.d(TAG, "shutdown task 2 finish...");
            return 1;
        });

        Future<Integer> result3 = pool.submit(() -> {
            Sout.d(TAG, "shutdown task 3 running...");
            Thread.sleep(1000);
            Sout.d(TAG, "shutdown task 3 finish...");
            return 1;
        });

        /**
         * 三个任务，只有两个线程，task 3 会进任务队列
         * 这个时候关闭线程池
         */
        pool.shutdown();
        Sout.d(TAG, "不会阻塞");
    }

    /**
     * 2025-05-01 10:16:30.651	pool-1-thread-1		TestShutDown	shutdown task 1 running...
     * 2025-05-01 10:16:30.651	pool-1-thread-2		TestShutDown	shutdown task 2 running...
     * 2025-05-01 10:16:31.654	pool-1-thread-1		TestShutDown	shutdown task 1 finish...
     * 2025-05-01 10:16:31.654	pool-1-thread-2		TestShutDown	shutdown task 2 finish...
     * 2025-05-01 10:16:31.654	pool-1-thread-1		TestShutDown	shutdown task 3 running...
     * 2025-05-01 10:16:32.634	main		TestShutDown	wait 2s
     * 2025-05-01 10:16:32.664	pool-1-thread-1		TestShutDown	shutdown task 3 finish...
     * @throws InterruptedException
     */
    public static void shutdownWait() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<Integer> result1 = pool.submit(() -> {
            Sout.d(TAG, "shutdown task 1 running...");
            Thread.sleep(1000);
            Sout.d(TAG, "shutdown task 1 finish...");
            return 1;
        });

        Future<Integer> result2 = pool.submit(() -> {
            Sout.d(TAG, "shutdown task 2 running...");
            Thread.sleep(1000);
            Sout.d(TAG, "shutdown task 2 finish...");
            return 1;
        });

        Future<Integer> result3 = pool.submit(() -> {
            Sout.d(TAG, "shutdown task 3 running...");
            Thread.sleep(1000);
            Sout.d(TAG, "shutdown task 3 finish...");
            return 1;
        });

        /**
         * 三个任务，只有两个线程，task 3 会进任务队列
         * 这个时候关闭线程池
         */
        pool.shutdown();
        pool.awaitTermination(2000, TimeUnit.MILLISECONDS);
        Sout.d(TAG, "wait 2s");
    }

    /**
     * 线程池状态变为 STOP
     * - 不会接收新任务
     * - 会将队列中的任务返回
     * - 并用 interrupt 的方式中断正在执行的任务
     * shutdownNow 会立即结束所有任务
     * 2025-05-01 10:19:25.914	pool-1-thread-1		TestShutDown	shutdown task 1 running...
     * 2025-05-01 10:19:25.914	pool-1-thread-2		TestShutDown	shutdown task 2 running...
     */
    public static void shutdownNow() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<Integer> result1 = pool.submit(() -> {
            Sout.d(TAG, "shutdown task 1 running...");
            Thread.sleep(1000);
            Sout.d(TAG, "shutdown task 1 finish...");
            return 1;
        });

        Future<Integer> result2 = pool.submit(() -> {
            Sout.d(TAG, "shutdown task 2 running...");
            Thread.sleep(1000);
            Sout.d(TAG, "shutdown task 2 finish...");
            return 1;
        });

        Future<Integer> result3 = pool.submit(() -> {
            Sout.d(TAG, "shutdown task 3 running...");
            Thread.sleep(1000);
            Sout.d(TAG, "shutdown task 3 finish...");
            return 1;
        });

        /**
         * 三个任务，只有两个线程，task 3 会进任务队列
         * 这个时候关闭线程池
         */
        List<Runnable> runnables = pool.shutdownNow();
        Sout.d(TAG, "runnables: " + runnables);

    }
}
