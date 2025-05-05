package htd.sharedmodeltool.juc.aqs;

import htd.utils.Sout;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-05-04 11:53
 **/
public class TestMySync {
    private static final String TAG = "TestMySync";

    /**
     * 2025-05-04 11:57:54.114	t1		TestMySync	t1 locking...
     * 2025-05-04 11:57:55.117	t1		TestMySync	t1 unlocking...
     * 2025-05-04 11:57:55.117	t2		TestMySync	t2 locking...
     * 2025-05-04 11:57:55.117	t2		TestMySync	t2 unlocking...
     * @param args
     */
    public static void main(String[] args) {
        MyLock lock = new MyLock();
        new Thread(() -> {
            lock.lock();
            // 不可重入
            // Sout.d(TAG, "t1 try locking1...");
            // lock.lock();
            // Sout.d(TAG, "t1 try locking2...");

            try {
                Sout.d(TAG, "t1 locking...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Sout.d(TAG, "t1 unlocking...");
                lock.unlock();
            }
        }, "t1").start();

        new Thread(() -> {
            lock.lock();
            try {
                Sout.d(TAG, "t2 locking...");
            } finally {
                Sout.d(TAG, "t2 unlocking...");
                lock.unlock();
            }
        }, "t2").start();
    }
}

// 自定义锁（不可重入锁）
class MyLock implements Lock {

    // 同步器类（独占锁）
    class MySync extends AbstractQueuedSynchronizer {
        @Override // 尝试获取锁
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                // 成功表示锁已经加上了，并设置 owner 为当前线程
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override // 尝试释放锁
        protected boolean tryRelease(int arg) {
            // 没有其他线程占用，直接释放
            setExclusiveOwnerThread(null);
            setState(0); // 注意这一行放在后面，这样之前的代码操作才有写屏障
            return true;
        }

        @Override // 是否持有锁
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        public Condition newCondition() {
            return new ConditionObject();
        }
    }

    private MySync mMySync = new MySync();

    @Override // 加锁（不成功，进入阻塞队列等待）
    public void lock() {
        mMySync.acquire(1);
    }

    @Override // 加锁，可打断
    public void lockInterruptibly() throws InterruptedException {
        mMySync.acquireInterruptibly(1);
    }

    @Override // 尝试加锁（一次）
    public boolean tryLock() {
        return mMySync.tryAcquire(1);
    }

    @Override // 尝试加锁，带超时时间
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return mMySync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override // 解锁
    public void unlock() {
        mMySync.release(1);
    }

    @Override // 创建条件变量
    public Condition newCondition() {
        return mMySync.newCondition();
    }
}
