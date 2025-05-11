package htd.sharedmodeltool.juc.reentrantlock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-05-10 16:18
 * <p>
 * Desc:
 */
public class TestCacheData {
}

class CachedData {
    private Object mData;
    // 是否有效，如果失效，需要重新计算 mData
    private volatile boolean mCacheValid;
    private final ReentrantReadWriteLock mReentrantReadWriteLock = new ReentrantReadWriteLock();

    public void processCachedData() {
        mReentrantReadWriteLock.readLock().lock();
        if (!mCacheValid) {
            // 获取写锁前必须释放读锁
            mReentrantReadWriteLock.readLock().unlock();
            mReentrantReadWriteLock.writeLock().lock();
            try {
                // 判断是否有其它线程已经获取了写锁、更新了缓存, 避免重复更新
                if (!mCacheValid) {
                    mData = new Object();
                    mCacheValid = true;
                }
                // 降级为读锁, 释放写锁, 这样能够让其它线程读取缓存
                mReentrantReadWriteLock.readLock().lock();
            } finally {
                mReentrantReadWriteLock.writeLock().unlock();
            }
        }
        // 自己用完数据, 释放读锁
        try {
            // use(mData);
        } finally {
            mReentrantReadWriteLock.readLock().unlock();
        }
    }
}