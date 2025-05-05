package htd.sharedmodeltool.thread_pool.thread_pool_executor;

import htd.utils.Sout;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-05-03 21:22
 **/
public class TestCatchException {
    private static final String TAG = "TestCatchException";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // test1();
        test2();
    }

    /**
     * 主动捉异常
     * 2025-05-03 21:25:24.360	pool-1-thread-1		TestCatchException	task1
     * 2025-05-03 21:25:24.361	pool-1-thread-1		TestCatchException	error:
     * java.lang.ArithmeticException: / by zero
     */
    private static void test1() {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        pool.submit(() -> {
            try {
                Sout.d(TAG, "task1");
                int i = 1 / 0;
            } catch (Exception e) {
                Sout.d(TAG, "error: ", e);
            }
        });
    }

    /**
     * Caused by: java.lang.ArithmeticException: / by zero
     */
    private static void test2() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        Future<Boolean> result = pool.submit(() -> {
            Sout.d(TAG, "task1");
            int i = 1 / 0;
            return true;
        });
        Sout.d(TAG, "result: " + result.get());

    }
}
