package htd.sharedmodeltubeside.thirteen_reentrant;

import htd.utils.Sout;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-29 7:40
 * <p>
 * Desc: ReentrantLock
 * 可中断性：对于像 synchronized 是没有办法中断的，如果没有获取到锁会一直等待锁，不能被打断
 * 而 ReentrantLock 是可以被打断的，结束等待锁的过程
 */
public class TestBreak {
    private static ReentrantLock sLock = new ReentrantLock();

    /**
     * 2025-03-29 07:42:27.778	main	主线程获取到了锁
     * 2025-03-29 07:42:27.778	t1		开始启动
     * 2025-03-29 07:42:28.778	main	开始执行打断
     * 2025-03-29 07:42:28.778	t1		等待锁的过程中被打断
     */
    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        Thread t1 = new Thread(() -> {
            Sout.d("开始启动");
            try {
                /**
                 * 获取到锁会正常向下运行
                 * 未获取到锁会在此一直等待
                 * 等待的过程中可以被打断，打断会抛出异常
                 * 这里如果调用 mLock.lock() 是不会被打断的
                 */
                sLock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Sout.d("等待锁的过程中被打断");
                // 抛出异常说明未获取到锁，被打断，直接退出
                return;
            }
            // 正常获取到锁，向下执行
            try {
                Sout.d("获得了锁");
            } finally {
                // 释放锁
                sLock.unlock();
            }
        }, "t1");

        // 主线程先获取到了锁
        sLock.lock();
        Sout.d("主线程获取到了锁");
        // 开启子线程
        t1.start();
        try {
            Thread.sleep(1000);
            // 开始打断子线程等待锁
            t1.interrupt();
            Sout.d("开始执行打断");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
