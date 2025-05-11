package htd.sharedmodeltool.juc.readwritelock;

import htd.utils.Sout;

import java.util.concurrent.locks.StampedLock;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-05-11 9:09
 * <p>
 * Desc:
 */
public class TestDataContainerStamped {
    public static void main(String[] args) {
        DataContainerStamped dataContainer = new DataContainerStamped(1);
        /**
         * 测试 读读
         * 输出结果，可以看到实际没有加读锁
         * 2025-05-11 09:16:39.396	t1		TestDataContainerStamped	optimistic read locking stamp: 256
         * 2025-05-11 09:16:39.398	t1		TestDataContainerStamped	read finish... stamp: 256 mData: 1
         * 2025-05-11 09:16:39.900	t2		TestDataContainerStamped	optimistic read locking stamp: 256
         * 2025-05-11 09:16:39.901	t2		TestDataContainerStamped	read finish... stamp: 256 mData: 1
         */
        // new Thread(() -> {
        //     dataContainer.read(1);
        // }, "t1").start();
        // try {
        //     Thread.sleep(500);
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }
        // new Thread(() -> {
        //     dataContainer.read(0);
        // }, "t2").start();

        /**
         * 测试 读-写 时优化读补加读锁
         * 2025-05-11 09:21:01.821	t1		TestDataContainerStamped	optimistic read locking stamp: 256
         * 2025-05-11 09:21:02.309	t2		TestDataContainerStamped	write lock stamp: 384
         * 2025-05-11 09:21:02.836	t1		TestDataContainerStamped	updating to read lock... stamp: 256
         * 2025-05-11 09:21:04.314	t2		TestDataContainerStamped	write unlock stamp: 384
         * 2025-05-11 09:21:04.314	t1		TestDataContainerStamped	read lock stamp: 513
         * 2025-05-11 09:21:05.321	t1		TestDataContainerStamped	read finish... stamp: 513 mData: 100
         * 2025-05-11 09:21:05.321	t1		TestDataContainerStamped	read unlock stamp: 513
         */
        new Thread(() -> {
            dataContainer.read(1000);
        }, "t1").start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            dataContainer.write(100);
        }, "t2").start();
    }
}

class DataContainerStamped {
    private static final String TAG = "TestDataContainerStamped";
    private int mData;
    private final StampedLock mStampedLock = new StampedLock();

    public DataContainerStamped(int data) {
        this.mData = data;
    }

    public int read(int readTime) {
        long stamp = mStampedLock.tryOptimisticRead();
        Sout.d(TAG, "optimistic read locking stamp: " + stamp);
        try {
            Thread.sleep(readTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (mStampedLock.validate(stamp)) {
            Sout.d(TAG, "read finish... stamp: " + stamp + " mData: " + mData);
            return mData;
        }
        // 锁升级 - 读锁
        Sout.d(TAG, "updating to read lock... stamp: " + stamp);
        try {
            stamp = mStampedLock.readLock();
            Sout.d(TAG, "read lock stamp: " + stamp);
            try {
                Thread.sleep(readTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Sout.d(TAG, "read finish... stamp: " + stamp + " mData: " + mData);
            return mData;
        } finally {
            Sout.d(TAG, "read unlock stamp: " + stamp);
            mStampedLock.unlockRead(stamp);
        }
    }

    public void write(int newData) {
        long stamp = mStampedLock.writeLock();
        Sout.d(TAG, "write lock stamp: " + stamp);
        try {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.mData = newData;
        } finally {
            Sout.d(TAG, "write unlock stamp: " + stamp);
            mStampedLock.unlockWrite(stamp);
        }
    }
}
