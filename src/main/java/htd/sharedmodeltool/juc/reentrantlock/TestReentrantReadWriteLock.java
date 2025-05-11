package htd.sharedmodeltool.juc.reentrantlock;

import htd.utils.Sout;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-05-10 0:27
 * <p>
 * Desc:
 */
public class TestReentrantReadWriteLock {
    private static final String TAG = "TestReentrantReadWriteLock";

    public static void main(String[] args) {
        // testReadAndRead();
        testReadAndWrite();
    }

    /**
     * 读读并行
     * 2025-05-10 00:36:13.675	t2		DataContainer	cfx 获取读锁
     * 2025-05-10 00:36:13.675	t1		DataContainer	cfx 获取读锁
     * 2025-05-10 00:36:13.675	t2		DataContainer	cfx 开始读取
     * 2025-05-10 00:36:13.675	t1		DataContainer	cfx 开始读取
     * 2025-05-10 00:36:14.688	t2		DataContainer	cfx 释放读锁
     * 2025-05-10 00:36:14.688	t1		DataContainer	cfx 释放读锁
     */
    private static void testReadAndRead() {
        DataContainer dataContainer = new DataContainer();
        new Thread(() -> {
            dataContainer.read();
        }, "t1").start();

        new Thread(() -> {
            dataContainer.read();
        }, "t2").start();
    }

    /**
     * 读写互斥
     * 2025-05-10 00:38:02.786	t1		DataContainer	cfx 获取读锁
     * 2025-05-10 00:38:02.786	t1		DataContainer	cfx 开始读取
     * 2025-05-10 00:38:02.795	t2		DataContainer	cfx 获取写锁
     * 2025-05-10 00:38:03.792	t1		DataContainer	cfx 释放读锁
     * 2025-05-10 00:38:03.792	t2		DataContainer	cfx 开始写入
     * 2025-05-10 00:38:04.803	t2		DataContainer	cfx 释放写锁
     */
    private static void testReadAndWrite() {
        DataContainer dataContainer = new DataContainer();
        new Thread(() -> {
            dataContainer.read();
        }, "t1").start();

        new Thread(() -> {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dataContainer.write();
        }, "t2").start();
    }
}

class DataContainer {
    private static final String TAG = "DataContainer";
    private Object mData;
    private ReentrantReadWriteLock mRWLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock mReadLock = mRWLock.readLock();
    private ReentrantReadWriteLock.WriteLock mWriteLock = mRWLock.writeLock();

    public Object read() {
        Sout.d(TAG, "cfx 获取读锁");
        mReadLock.lock();
        try {
            Sout.d(TAG, "cfx 开始读取");
            Thread.sleep(1000);
            return mData;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            Sout.d(TAG, "cfx 释放读锁");
            mReadLock.unlock();
        }
        return null;
    }

    public void write() {
        Sout.d(TAG, "cfx 获取写锁");
        mWriteLock.lock();
        try {
            Sout.d(TAG, "cfx 开始写入");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            Sout.d(TAG, "cfx 释放写锁");
            mWriteLock.unlock();
        }
    }
}
