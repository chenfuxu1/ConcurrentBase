package htd.sharedmodeltool.thread_pool.thread_pool_executor;

import htd.utils.Sout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-20 21:38
 *
 * 固定大小的线程池
 * 特点
 * 核心线程数 == 最大线程数（没有救急线程被创建），因此也无需超时时间
 * 阻塞队列是无界的，可以放任意数量的任务
 * 线程运行后不会主动结束线程
 *
 * 适用于任务量已知，相对耗时的任务
 **/
public class TestNewFixedThreadPool {
    private static final String TAG = "TestNewFixedThreadPool";

    public static void main(String[] args) {
        /**
         * 2025-04-20 21:40:09.803	pool-1-thread-1		TestNewFixedThreadPool	任务 1
         * 2025-04-20 21:40:09.803	pool-1-thread-2		TestNewFixedThreadPool	任务 2
         * 2025-04-20 21:40:09.803	pool-1-thread-1		TestNewFixedThreadPool	任务 3
         */
        // test1();

        /**
         * 2025-04-20 21:42:14.562	 线程池 2	TestNewFixedThreadPool	b
         * 2025-04-20 21:42:14.562	 线程池 1	TestNewFixedThreadPool	a
         * 2025-04-20 21:42:14.562	 线程池 2	TestNewFixedThreadPool	c
         */
        test2();
    }

    // 自定义线程池的名称
    private static void test2() {
        ExecutorService pool = Executors.newFixedThreadPool(2, new ThreadFactory() {
            private AtomicInteger num = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, " 线程池 " + num.getAndIncrement());
            }
        });
        pool.execute(() -> {
            Sout.d(TAG,"a");
        });
        pool.execute(() -> {
            Sout.d(TAG,"b");
        });
        pool.execute(() -> {
            Sout.d(TAG,"c");
        });
    }

    private static void test1() {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        pool.execute(() -> {
            Sout.d(TAG,"任务 1");
        });

        pool.execute(() -> {
            Sout.d(TAG,"任务 2");
        });

        pool.execute(() -> {
            Sout.d(TAG,"任务 3");
        });
    }
}
