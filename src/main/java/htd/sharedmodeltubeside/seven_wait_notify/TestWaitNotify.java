package htd.sharedmodeltubeside.seven_wait_notify;

import htd.utils.Sout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-23 15:41
 * <p>
 * obj.wait() 让进入 object 监视器的线程到 waitSet 等待
 * obj.notify() 在 object 上正在 waitSet 等待的线程中挑一个唤醒
 * obj.notifyAll() 让 object 上正在 waitSet 等待的线程全部唤醒
 * 它们都是线程之间进行协作的手段，都属于 Object 对象的方法。必须获得此对象的锁，才能调用这几个方法
 * <p>
 * wait() 方法会释放对象的锁，进入 WaitSet 等待区，从而让其他线程就有机会获取到对象的锁
 * 无限制等待，直到 notify 为止
 * <p>
 * wait(long n) 有时限的等待，直到 n 毫秒以后结束等待，或者被 notify
 **/
public class TestWaitNotify {
    private static final Object OBJ = new Object();

    /**
     * 2025-03-23 15:43:22.985	t1		开始执行
     * 2025-03-23 15:43:22.985	t2		开始执行
     * 2025-03-23 15:43:24.970	main	开始唤醒在 obj 上等待的线程
     * 2025-03-23 15:43:24.970	t2		执行结束
     * 2025-03-23 15:43:24.970	t1		执行结束
     */
    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (OBJ) {
                Sout.d("开始执行");
                try {
                    // 让 t1 线程在 obj 上一直等待下去，直到 notify 唤醒
                    OBJ.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Sout.d("执行结束");
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (OBJ) {
                Sout.d("开始执行");
                try {
                    // 让 t2 线程在 obj 上一直等待下去，直到 notify 唤醒
                    // 因为 t1 已经让出锁了，在休息区，所以 t2 可以拿到锁
                    OBJ.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Sout.d("执行结束");
            }
        }, "t2").start();


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         * 执行到此处，t1 t2 线程都在休息区了，并让出了锁，等待唤醒
         * 所以主线程可以拿到锁对象
         */
        Sout.d("开始唤醒在 obj 上等待的线程");
        synchronized (OBJ) {
            // 唤醒 OBJ 上的一个线程
            // OBJ.notify();

            OBJ.notifyAll(); // 唤醒 OBJ 上所有的等待线程
        }
    }
}
