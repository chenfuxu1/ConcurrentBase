package htd.sharedmodeltubeside.twelve_active;

import htd.utils.Sout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-24 23:39
 * <p>
 * Desc: 活锁
 * 活锁出现在两个线程互相改变对方的结束条件，最后谁也无法结束
 */
public class TestLiveLock {
    private static int sCount = 10;
    private static final Object LOCK = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            // 期望减到 0 退出循环
            while (sCount > 0) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sCount--;
                Sout.d("sCount = " + sCount);
            }
        }, "t1").start();
        new Thread(() -> {
            // 期望大于 20 退出循环
            while (sCount < 20) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sCount++;
                Sout.d("sCount = " + sCount);
            }
        }, "t2").start();
    }
}
