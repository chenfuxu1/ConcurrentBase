package htd.sharedmodeltubeside.thirteen_reentrant;

import htd.utils.Sout;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-29 7:47
 * <p>
 * Desc: ReentrantLock
 * 锁超时
 * 锁超时相当于：未获得到锁的情况下可以设置等待的时间，超过时间就不再等待
 * 避免死锁的发生
 * tryLock(long timeout, TimeUnit unit)：
 * 会等待一段时间
 */
public class TestLockTimeout2 {
    private static ReentrantLock sLock = new ReentrantLock();

    /**
     * 2025-03-29 07:48:23.825	main	主线程获取到了锁
     * 2025-03-29 07:48:23.825	t2		开始启动
     * 2025-03-29 07:48:24.836	t2		获取等待锁 1s 后失败， 返回
     */
    public static void main(String[] args) {
        Thread t2 = new Thread(() -> {
            Sout.d("开始启动");
            /**
             * sLock.tryLock(long timeout, TimeUnit unit):
             * 1.会尝试等待 timeout，因为主线程获取锁，并等待 2s
             * 所以这里等待 1s 获取不到锁, 会返回 false
             * 2.在等待的过程中也支持 interrupt，打断会抛出异常
             */
            try {
                if (!sLock.tryLock(1, TimeUnit.SECONDS)) {
                    Sout.d("获取等待锁 1s 后失败， 返回");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Sout.d("获取锁成功");
            } finally {
                // 释放锁
                sLock.unlock();
            }

        }, "t2");

        // 主线程获取到了锁
        sLock.lock();
        Sout.d("主线程获取到了锁");
        t2.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            sLock.unlock();
        }
    }
}
