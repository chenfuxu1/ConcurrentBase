package htd.sharedmodeltubeside.thirteen_reentrant;

import htd.utils.Sout;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-29 7:44
 * <p>
 * ReentrantLock
 * 锁超时
 * 锁超时相当于：未获得到锁的情况下可以设置等待的时间，超过时间就不再等待
 * 避免死锁的发生
 */
public class TestLockTimeout {
    private static ReentrantLock sLock = new ReentrantLock();

    /**
     * 2025-03-29 07:45:27.107	main	主线程获取到了锁
     * 2025-03-29 07:45:27.108	t1		开始启动
     * 2025-03-29 07:45:27.108	t1		获取锁失败，返回
     */
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            Sout.d("开始启动");
            /**
             * sLock.tryLock():
             * 该方法不会等待，获取到了锁，返回 true，否则返回 false
             * 由于主线程获取到了锁，这里获取不到锁了
             * 直接返回 false
             */
            if (!sLock.tryLock()) {
                Sout.d("获取锁失败，返回");
                return;
            }
            try {
                Sout.d("获取锁成功");
            } finally {
                // 释放锁
                sLock.unlock();
            }

        }, "t1");

        // 主线程获取到了锁
        sLock.lock();
        Sout.d("主线程获取到了锁");
        t1.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            sLock.unlock();
        }
    }
}
