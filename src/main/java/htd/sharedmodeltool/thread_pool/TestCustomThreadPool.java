package htd.sharedmodeltool.thread_pool;

import htd.utils.Sout;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-19 17:55
 **/
public class TestCustomThreadPool {
    private static final String TAG = "TestCustomThreadPool";

    public static void main(String[] args) {
        /**
         * 2025-04-19 18:22:40.374	main		ThreadPool	新增 worker：Worker{mTask=htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@6ce253f1}1406718218 task: htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@6ce253f1
         * 2025-04-19 18:22:40.374	main		ThreadPool	新增 worker：Worker{mTask=htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@7cca494b}2074407503 task: htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@7cca494b
         * 2025-04-19 18:22:40.374	Thread-0	ThreadPool	cfx 正在执行：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@6ce253f1
         * 2025-04-19 18:22:40.374	main		BlockingQueue	加入任务队列：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@3b9a45b3
         * 2025-04-19 18:22:40.374	Thread-1	ThreadPool	cfx 正在执行：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@7cca494b
         * 2025-04-19 18:22:40.374	Thread-0	TestCustomThreadPool	当前的任务：0
         * 2025-04-19 18:22:40.374	main		BlockingQueue	加入任务队列：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@7699a589
         * 2025-04-19 18:22:40.374	Thread-1	TestCustomThreadPool	当前的任务：1
         * 2025-04-19 18:22:40.374	main		BlockingQueue	加入任务队列：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@58372a00
         * 2025-04-19 18:22:40.374	Thread-0	BlockingQueue	移除阻塞队列 mDeque 头部元素：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@3b9a45b3
         * 2025-04-19 18:22:40.374	Thread-0	ThreadPool	cfx 正在执行：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@3b9a45b3
         * 2025-04-19 18:22:40.374	Thread-1	BlockingQueue	移除阻塞队列 mDeque 头部元素：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@7699a589
         * 2025-04-19 18:22:40.374	Thread-0	TestCustomThreadPool	当前的任务：2
         * 2025-04-19 18:22:40.374	Thread-1	ThreadPool	cfx 正在执行：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@7699a589
         * 2025-04-19 18:22:40.374	Thread-0	BlockingQueue	移除阻塞队列 mDeque 头部元素：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@58372a00
         * 2025-04-19 18:22:40.374	Thread-1	TestCustomThreadPool	当前的任务：3
         * 2025-04-19 18:22:40.374	Thread-0	ThreadPool	cfx 正在执行：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@58372a00
         * 2025-04-19 18:22:40.374	Thread-0	TestCustomThreadPool	当前的任务：4
         * 2025-04-19 18:22:41.389	Thread-1	ThreadPool	cfx worker 被移除：Worker{mTask=null}2074407503
         * 2025-04-19 18:22:41.389	Thread-0	ThreadPool	cfx worker 被移除：Worker{mTask=null}1406718218
         */
        // test1();

        /**
         * 2025-04-19 18:41:14.999	main		ThreadPool	新增 worker：Worker{mTask=htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@6ce253f1}1406718218 task: htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@6ce253f1
         * 2025-04-19 18:41:15.000	main		ThreadPool	新增 worker：Worker{mTask=htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@7cca494b}2074407503 task: htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@7cca494b
         * 2025-04-19 18:41:15.000	Thread-0	ThreadPool	cfx 正在执行：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@6ce253f1
         * 2025-04-19 18:41:15.000	main		BlockingQueue	加入任务队列：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@3b9a45b3
         * 2025-04-19 18:41:15.000	Thread-0	TestCustomThreadPool	当前的任务：0
         * 2025-04-19 18:41:15.000	Thread-1	ThreadPool	cfx 正在执行：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@7cca494b
         * 2025-04-19 18:41:15.000	Thread-0	BlockingQueue	移除阻塞队列 mDeque 头部元素：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@3b9a45b3
         * 2025-04-19 18:41:15.000	Thread-1	TestCustomThreadPool	当前的任务：1
         * 2025-04-19 18:41:15.001	Thread-0	ThreadPool	cfx 正在执行：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@3b9a45b3
         * 2025-04-19 18:41:15.001	main		BlockingQueue	加入任务队列：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@58372a00
         * 2025-04-19 18:41:15.001	Thread-0	TestCustomThreadPool	当前的任务：2
         * 2025-04-19 18:41:15.001	main		BlockingQueue	加入任务队列：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@4dd8dc3
         * 2025-04-19 18:41:15.001	main		BlockingQueue	加入任务队列：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@6d03e736
         * 2025-04-19 18:41:15.001	main		BlockingQueue	加入任务队列：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@568db2f2
         * 2025-04-19 18:41:15.001	main		BlockingQueue	加入任务队列：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@378bf509
         * 2025-04-19 18:41:15.001	main		BlockingQueue	加入任务队列：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@5fd0d5ae
         * 2025-04-19 18:41:15.001	main		BlockingQueue	加入任务队列：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@2d98a335
         * 2025-04-19 18:41:15.001	main		BlockingQueue	加入任务队列：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@16b98e56
         * 2025-04-19 18:41:15.001	main		BlockingQueue	加入任务队列：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@7ef20235
         * 2025-04-19 18:41:15.001	main		BlockingQueue	加入任务队列：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@27d6c5e0
         * 2025-04-19 18:41:15.001	Thread-1	BlockingQueue	移除阻塞队列 mDeque 头部元素：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@58372a00
         * 2025-04-19 18:41:15.002	Thread-1	ThreadPool	cfx 正在执行：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@58372a00
         * 2025-04-19 18:41:15.002	Thread-0	BlockingQueue	移除阻塞队列 mDeque 头部元素：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@4dd8dc3
         * 2025-04-19 18:41:15.002	Thread-1	TestCustomThreadPool	当前的任务：3
         * 2025-04-19 18:41:15.002	Thread-0	ThreadPool	cfx 正在执行：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@4dd8dc3
         * 2025-04-19 18:41:15.002	Thread-1	BlockingQueue	移除阻塞队列 mDeque 头部元素：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@6d03e736
         * 2025-04-19 18:41:15.002	Thread-0	TestCustomThreadPool	当前的任务：4
         * 2025-04-19 18:41:15.002	Thread-1	ThreadPool	cfx 正在执行：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@6d03e736
         * 2025-04-19 18:41:15.002	Thread-0	BlockingQueue	移除阻塞队列 mDeque 头部元素：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@568db2f2
         * 2025-04-19 18:41:15.002	Thread-1	TestCustomThreadPool	当前的任务：5
         * 2025-04-19 18:41:15.002	Thread-0	ThreadPool	cfx 正在执行：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@568db2f2
         * 2025-04-19 18:41:15.002	Thread-1	BlockingQueue	移除阻塞队列 mDeque 头部元素：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@378bf509
         * 2025-04-19 18:41:15.002	Thread-0	TestCustomThreadPool	当前的任务：6
         * 2025-04-19 18:41:15.002	Thread-1	ThreadPool	cfx 正在执行：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@378bf509
         * 2025-04-19 18:41:15.002	Thread-0	BlockingQueue	移除阻塞队列 mDeque 头部元素：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@5fd0d5ae
         * 2025-04-19 18:41:15.002	Thread-1	TestCustomThreadPool	当前的任务：7
         * 2025-04-19 18:41:15.002	Thread-0	ThreadPool	cfx 正在执行：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@5fd0d5ae
         * 2025-04-19 18:41:15.002	Thread-1	BlockingQueue	移除阻塞队列 mDeque 头部元素：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@2d98a335
         * 2025-04-19 18:41:15.003	Thread-0	TestCustomThreadPool	当前的任务：8
         * 2025-04-19 18:41:15.003	Thread-1	ThreadPool	cfx 正在执行：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@2d98a335
         * 2025-04-19 18:41:15.003	Thread-0	BlockingQueue	移除阻塞队列 mDeque 头部元素：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@16b98e56
         * 2025-04-19 18:41:15.003	Thread-1	TestCustomThreadPool	当前的任务：9
         * 2025-04-19 18:41:15.003	Thread-0	ThreadPool	cfx 正在执行：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@16b98e56
         * 2025-04-19 18:41:15.003	Thread-1	BlockingQueue	移除阻塞队列 mDeque 头部元素：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@7ef20235
         * 2025-04-19 18:41:15.003	Thread-0	TestCustomThreadPool	当前的任务：10
         * 2025-04-19 18:41:15.003	Thread-1	ThreadPool	cfx 正在执行：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@7ef20235
         * 2025-04-19 18:41:15.003	Thread-0	BlockingQueue	移除阻塞队列 mDeque 头部元素：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@27d6c5e0
         * 2025-04-19 18:41:15.003	Thread-1	TestCustomThreadPool	当前的任务：11
         * 2025-04-19 18:41:15.003	Thread-0	ThreadPool	cfx 正在执行：htd.sharedmodeltool.thread_pool.TestCustomThreadPool$$Lambda$1/303563356@27d6c5e0
         * 2025-04-19 18:41:15.003	Thread-0	TestCustomThreadPool	当前的任务：12
         * 2025-04-19 18:41:16.007	Thread-1	ThreadPool	cfx worker 被移除：Worker{mTask=null}2074407503
         * 2025-04-19 18:41:16.007	Thread-0	ThreadPool	cfx worker 被移除：Worker{mTask=null}1406718218
         */
        // test2();

        test3();
    }

    // 拒绝策略
    private static void test3() {
        ThreadPool threadPool = new ThreadPool(1, 2000, TimeUnit.MILLISECONDS, 1, (queue, task) -> {
            // 1.死等的情况
            // queue.put(task);

            // 2.带超时等待
            // queue.offer(task, 1500, TimeUnit.MILLISECONDS);

            // 3.让调用者放弃任务执行
            // Sout.d("放弃任务：" + task);

            // 4.让调用者抛出异常
            // throw new RuntimeException("任务执行失败：" + task);

            // 5.让调用者自己执行任务
            task.run();
        });
        // 创建 15 个任务，超过队列的容量为 10
        for (int i = 0; i < 3; i++) {
            int j = i;
            threadPool.execute2(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Sout.d(TAG,"当前的任务：" + j);
            });
        }
    }

    // 测试当前任务队列已满的情况
    private static void test2() {
        ThreadPool threadPool = new ThreadPool(2, 1000, TimeUnit.MILLISECONDS, 10);
        // 创建 13 个任务，超过队列的容量为 10
        for (int i = 0; i < 13; i++) {
            int j = i;
            threadPool.execute(() -> {
                Sout.d(TAG, "当前的任务：" + j);
            });
        }
    }

    private static void test1() {
        ThreadPool threadPool = new ThreadPool(2, 1000, TimeUnit.MILLISECONDS, 10);
        for (int i = 0; i < 5; i++) {
            int j = i;
            threadPool.execute(() -> {
                Sout.d(TAG, "当前的任务：" + j);
            });
        }
    }
}

// 拒绝策略
@FunctionalInterface
interface RejectPolicy<T> {
    void reject(BlockingQueue<T> queue, T task);
}

class ThreadPool {
    private static final String TAG = "ThreadPool";
    // 任务队列
    private BlockingQueue<Runnable> mTaskQueue;

    // 线程集合
    private HashSet<Worker> mWorkers = new HashSet<>();

    // 核心线程数
    private int mCoreSize;

    // 获取任务的超时时间
    private long mTimeout;

    // 时间单元
    private TimeUnit mTimeUnit;

    // 拒绝策略接口
    private RejectPolicy<Runnable> mRejectPolicy;

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCapacity) {
        mCoreSize = coreSize;
        mTimeout = timeout;
        mTimeUnit = timeUnit;
        this.mTaskQueue = new BlockingQueue<>(queueCapacity);
    }

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCapacity, RejectPolicy<Runnable> rejectPolicy) {
        mCoreSize = coreSize;
        mTimeout = timeout;
        mTimeUnit = timeUnit;
        this.mTaskQueue = new BlockingQueue<>(queueCapacity);
        this.mRejectPolicy = rejectPolicy;
    }

    // 执行任务
    public void execute(Runnable task) {
        synchronized (this) {
            // 当任务数没有超过 coreSize 时，直接交给 worker 执行
            if (mWorkers.size() < mCoreSize) {
                Worker worker = new Worker(task);
                Sout.d(TAG, "新增 worker：" + worker + " task: " + task);
                mWorkers.add(worker);
                worker.start();
            } else {
                // 当任务数超过 coreSize 时，加入任务队列暂存
                mTaskQueue.put(task);
            }

        }
    }

    /**
     * 执行任务
     * 策略模式
     */
    public void execute2(Runnable task) {
        synchronized (this) {
            // 当任务数没有超过 coreSize 时，直接交给 worker 执行
            if (mWorkers.size() < mCoreSize) {
                Worker worker = new Worker(task);
                Sout.d(TAG, "新增 worker：" + worker + " task: " + task);
                mWorkers.add(worker);
                worker.start();
            } else {
                /**
                 * 1.死等
                 * 2.带超时等待
                 * 3.让调用者放弃任务执行
                 * 4.让调用者抛出异常
                 * 5.让调用者自己执行任务
                 */
                mTaskQueue.tryPut(mRejectPolicy, task);
            }

        }
    }

    class Worker extends Thread {
        private Runnable mTask;

        public Worker(Runnable task) {
            this.mTask = task;
        }

        @Override
        public void run() {
            // 执行任务
            // 1.当 task 不为空，执行任务
            // 2.当 task 执行完毕，再接着从任务队列获取任务并执行
            // while (mTask != null || (mTask = mTaskQueue.take()) != null) {
            while (mTask != null || (mTask = mTaskQueue.pull(mTimeout, mTimeUnit)) != null) {
                try {
                    Sout.d(TAG,"cfx 正在执行：" + mTask);
                    mTask.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mTask = null;
                }
            }
            // 如果任务执行完了，将当前的 worker 移除掉
            synchronized (mWorkers) {
                Sout.d(TAG, "cfx worker 被移除：" + this);
                mWorkers.remove(this);
            }
        }

        @Override
        public String toString() {
            return "Worker{" +
                    "mTask=" + mTask +
                    '}' + hashCode();
        }
    }
}

/**
 * 阻塞队列
 * @param <T>
 */
class BlockingQueue<T> {
    private static final String TAG = "BlockingQueue";
    // 1.任务队列
    private Deque<T> mDeque = new ArrayDeque<>();

    // 2.锁
    private ReentrantLock mLock = new ReentrantLock();

    // 3.生产者条件变量
    private Condition mFullWaitSet = mLock.newCondition();

    // 4.消费者条件变量
    private Condition mEmptyWaitSet = mLock.newCondition();

    // 5.容量
    private int mCapacity;

    public BlockingQueue(int capacity) {
        mCapacity = capacity;
    }

    // 阻塞获取
    public T take() {
        mLock.lock();
        try {
            // 如果队列为空
            while (mDeque.isEmpty()) {
                try {
                    // 开始等待
                    mEmptyWaitSet.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 队列不为空，获取阻塞队列的头部元素
            T t = mDeque.removeFirst(); // 这里移除头部的元素
            // 唤醒 mFullWaitSet
            mFullWaitSet.signal();
            return t;
        } finally {
            mLock.unlock();
        }
    }

    // 带超时的阻塞获取, 无需永久的等待
    public T pull(long timeout, TimeUnit unit) {
        mLock.lock();
        try {
            // 将超时时间统一转换为 ns
            long nanos = unit.toNanos(timeout);
            // 如果队列为空
            while (mDeque.isEmpty()) {
                try {
                    if (nanos <= 0) {
                        return null;
                    }
                    // 开始等待, 会返回 超时时间 - 已经等待的时间
                    nanos = mEmptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 队列不为空，获取阻塞队列的头部元素
            T t = mDeque.removeFirst(); // 这里移除头部的元素
            Sout.d(TAG, "移除阻塞队列 mDeque 头部元素：" + t);
            // 唤醒 mFullWaitSet
            mFullWaitSet.signal();
            return t;
        } finally {
            mLock.unlock();
        }
    }

    // 阻塞添加
    public void put(T element) {
        mLock.lock();

        try {
            // 队列满了，不能再添加了
            while (mDeque.size() == mCapacity) {
                try {
                    Sout.d(TAG, "等待加入任务队列..." + element);
                    // 等待队列不满时，开始添加
                    mFullWaitSet.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Sout.d(TAG, "加入任务队列：" + element);
            // 队列不满时，开始添加
            mDeque.addLast(element); // 从尾部开始添加
            // 唤醒阻塞的 mEmptyWaitSet 条件
            mEmptyWaitSet.signal();
        } finally {
            mLock.unlock();
        }
    }

    // 带超时时间的阻塞添加
    public boolean offer(T task, long timeout, TimeUnit timeUnit) {
        mLock.lock();
        try {
            long nanos = timeUnit.toNanos(timeout);
            // 队列满了，不能再添加了
            while (mDeque.size() == mCapacity) {
                try {
                    Sout.d(TAG, "等待加入任务队列... nanos: " + nanos + " task: " + task);
                    if (nanos <= 0) {
                        return false;
                    }
                    // 等待队列不满时，开始添加
                    nanos = mFullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Sout.d(TAG, "加入任务队列：" + task);
            // 队列不满时，开始添加
            mDeque.addLast(task); // 从尾部开始添加
            // 唤醒阻塞的 mEmptyWaitSet 条件
            mEmptyWaitSet.signal();
            return true;
        } finally {
            mLock.unlock();
        }
    }

    // 获取队列的大小
    public int size() {
        mLock.lock();
        try {
            return mDeque.size();
        } finally {
            mLock.unlock();
        }
    }

    // 策略模式的添加方法
    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        mLock.lock();
        try {
            if (mDeque.size() == mCapacity) {
                // 将权利下放处理，因为有多种的处理方式，不只是一种一直死等的情况
                rejectPolicy.reject(this, task);
            } else {
                // 有空闲
                Sout.d(TAG,"加入任务队列：" + task);
                mDeque.addLast(task);
                mEmptyWaitSet.signal();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mLock.unlock();
        }
    }
}
