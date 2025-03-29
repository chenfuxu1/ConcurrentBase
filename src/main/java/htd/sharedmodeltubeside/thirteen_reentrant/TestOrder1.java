package htd.sharedmodeltubeside.thirteen_reentrant;

import htd.utils.Sout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-29 8:55
 * <p>
 * Desc:
 * 同步模式之顺序控制
 * 先打印 2 再打印 1
 * wait notify 版
 */
public class TestOrder1 {
    private static Object sObject = new Object();
    // 标记 t2 线程是否运行过
    private static boolean sT2Runned = false;

    /**
     * 2025-03-29 08:56:55.056	t2	2
     * 2025-03-29 08:56:55.056	t1	1
     */
    public static void main(String[] args) {
        /**
         * 1. t1 先执行，sT2Runned 为 false，会执行 sObject.wait() 等待
         * 同时释放锁，这时 t2 执行，执行完 sObject.notify() 唤醒 t1 ，t1 接着执行
         *
         * 2、t2 先执行，执行完 sT2Runned 为 true，这时 sObject.notify() 为虚假唤醒，没有线程在休息室等待唤醒
         * 然后会直接执行 t1
         */
        Thread t1 = new Thread(() -> {
            synchronized (sObject) {
                // 如果 t2 没有执行过
                while (!sT2Runned) {
                    try {
                        // 让 t1 先等待
                        sObject.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Sout.d("1");
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (sObject) {
                Sout.d("2");
                sT2Runned = true;
                sObject.notify();
            }
        }, "t2");

        t1.start();
        t2.start();
    }
}
