package htd.sharedmodeltool.thread_pool.thread_pool_executor;

import htd.utils.Sout;

import java.util.concurrent.SynchronousQueue;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-20 21:44
 *
 * 带缓存的线程池
 * 特点
 *   核心线程数是 0，最大线程数是 Integer.MAX_VALUE，救急线程的空闲生存时间是 60s，意味着
 *      全部都是救急线程（60s 后可以回收）
 *      救急线程可以无限创建
 *   队列采用了 SynchronousQueue 实现特点是，它没有容量，没有线程来取是放不进去的（一手交钱、一手交货）
 *
 *   public static ExecutorService newCachedThreadPool() {
 *      return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
 *                  60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
 * }
 * 整个线程池表现为线程数会根据任务量不断增长，没有上限，当任务执行完毕，空闲 1分钟后释放线
 * 程。 适合任务数比较密集，但每个任务执行时间较短的情况
 **/
public class TestNewCachedThreadPool {
    private static final String TAG = "TestNewCachedThreadPool";

    /**
     * 2025-04-20 22:03:59.214	t1	TestNewCachedThreadPool	putting 1
     * 2025-04-20 22:04:00.201	t2	TestNewCachedThreadPool	taking...1
     * 2025-04-20 22:04:00.201	t1	TestNewCachedThreadPool	putted...1
     * 2025-04-20 22:04:00.201	t1	TestNewCachedThreadPool	putting 2
     * 2025-04-20 22:04:01.205	t3	TestNewCachedThreadPool	taking...2
     * 2025-04-20 22:04:01.205	t1	TestNewCachedThreadPool	putted...2
     * @param args
     */
    public static void main(String[] args) {
        SynchronousQueue<Integer> integers = new SynchronousQueue<>();
        new Thread(() -> {
            try {
                Sout.d(TAG,"putting " + 1);
                integers.put(1);
                Sout.d(TAG,"putted..." + 1);

                Sout.d(TAG,"putting " + 2);
                integers.put(2);
                Sout.d(TAG,"putted..." + 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "t1").start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            try {
                Sout.d(TAG,"taking..." + 1);
                integers.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2").start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            try {
                Sout.d(TAG,"taking..." + 2);
                integers.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t3").start();
    }
}

