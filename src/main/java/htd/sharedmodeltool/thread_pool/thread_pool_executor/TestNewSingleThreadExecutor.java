package htd.sharedmodeltool.thread_pool.thread_pool_executor;

import htd.utils.Sout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-20 22:11
 *
 * 单线程线程池：NewSingleThreadExecutor
 * public static ExecutorService newSingleThreadExecutor() {
 *      return new FinalizableDelegatedExecutorService(new ThreadPoolExecutor(1, 1,
 *                  0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()));
 * }
 *使用场景：
 * 希望多个任务排队执行。线程数固定为 1，任务数多于 1 时，会放入无界队列排队。任务执行完毕，这唯一的线程
 * 也不会被释放。
 *
 * 区别：
 * 自己创建一个单线程串行执行任务，如果任务执行失败而终止那么没有任何补救措施，而线程池还会新建一
 * 个线程，保证池的正常工作
 * Executors.newSingleThreadExecutor() 线程个数始终为 1，不能修改
 *      FinalizableDelegatedExecutorService 应用的是装饰器模式，只对外暴露了 ExecutorService 接口，因
 *      此不能调用 ThreadPoolExecutor 中特有的方法
 * Executors.newFixedThreadPool(1) 初始时为1，以后还可以修改
 *      对外暴露的是 ThreadPoolExecutor 对象，可以强转后调用 setCorePoolSize 等方法进行修改
 **/
public class TestNewSingleThreadExecutor {
    private static final String TAG = "TestNewSingleThreadExecutor";

    /**
     * 2025-04-20 22:13:47.671	pool-1-thread-1		TestNewSingleThreadExecutor	1
     * 2025-04-20 22:13:47.671	pool-1-thread-2		TestNewSingleThreadExecutor	2
     * 2025-04-20 22:13:47.671	pool-1-thread-3		TestNewSingleThreadExecutor	3
     * 2025-04-20 22:13:47.671	pool-1-thread-3		TestNewSingleThreadExecutor	4
     * Exception in thread "pool-1-thread-1" Exception in thread "pool-1-thread-2" java.lang.ArithmeticException: / by zero
     * 	at htd.sharedmodeltool.thread_pool.thread_pool_executor.TestNewSingleThreadExecutor.lambda$main$0(TestNewSingleThreadExecutor.java:38)
     * 	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
     * 	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
     * 	at java.lang.Thread.run(Thread.java:748)
     * java.lang.ArithmeticException: / by zero
     * 	at htd.sharedmodeltool.thread_pool.thread_pool_executor.TestNewSingleThreadExecutor.lambda$main$1(TestNewSingleThreadExecutor.java:42)
     * 	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
     * 	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
     * 	at java.lang.Thread.run(Thread.java:748)
     * @param args
     */
    public static void main(String[] args) {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.execute(() -> {
            Sout.d(TAG,"1");
            int i = 1 / 0;
        });
        pool.execute(() -> {
            Sout.d(TAG,"2");
            int i = 1 / 0;

        });
        pool.execute(() -> {
            Sout.d(TAG,"3");
        });
        pool.execute(() -> {
            Sout.d(TAG,"4");
        });
    }
}
