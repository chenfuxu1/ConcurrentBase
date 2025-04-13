package htd.sharedmodelnolock.seven_atomic_add;

import htd.utils.Sout;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-13 8:41
 * <p>
 * Desc: 源码之 LongAdder
 * LongAdder 是并发大师 @author Doug Lea （大哥李）的作品，设计的非常精巧
 * LongAdder 类有几个关键域
 * <p>
 * 累加单元数组, 懒惰初始化
 * transient volatile Cell[] cells;
 * <p>
 * 基础值, 如果没有竞争, 则用 cas 累加这个域
 * transient volatile long base;
 * <p>
 * 在 cells 创建或扩容时, 置为 1, 表示加锁
 * transient volatile int cellsBusy;
 */
public class TestAtomicLongAdder {
    /**
     * 2025-04-13 08:44:57.825	t1		t1 开始
     * 2025-04-13 08:44:57.825	t2		t2 开始...
     * 2025-04-13 08:44:57.825	t1		t1 lock...
     * 2025-04-13 08:44:58.830	t1		t1 解锁...
     * 2025-04-13 08:44:58.830	t2		t2 lock...
     * 2025-04-13 08:44:58.830	t2		t2 解锁...
     */
    public static void main(String[] args) {
        LockCas lock = new LockCas();
        new Thread(() -> {
            Sout.d("t1 开始");
            lock.lock();
            try {
                Sout.d("t1 lock...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t1").start();

        new Thread(() -> {
            Sout.d("t2 开始...");
            lock.lock();
            try {
                Sout.d("t2 lock...");
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }
}

/**
 * cas 锁的实现原理
 * 一般底层这样实现，我们不要用于实践，会有性能问题
 */
class LockCas {
    // 0：表示无锁 1：表示锁住
    private AtomicInteger mState = new AtomicInteger(0);

    // 加锁
    public void lock() {
        while (true) {
            if (mState.compareAndSet(0, 1)) {
                break;
            }
        }
    }

    /**
     * 解锁
     * 无需原子操作，因为解锁的操作只在当前持有锁的线程进行
     */
    public void unlock() {
        Sout.d(Thread.currentThread().getName() + " 解锁...");
        mState.set(0);
    }
}
