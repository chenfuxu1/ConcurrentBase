package htd.sharedmodeltubeside.twelve_active;

import htd.utils.Sout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-24 23:24
 * <p>
 * Desc: 死锁
 * 有这样的情况：一个线程需要同时获取多把锁，这时就容易发生死锁
 * t1 线程 获得 A对象 锁，接下来想获取 B对象 的锁 t2 线程 获得 B对象 锁，接下来想获取 A对象 的锁
 */
public class TestDeadLock {
    /**
     * 2025-03-24 23:25:23.300	t1	lock A
     * 2025-03-24 23:25:23.300	t2	lock B
     */
    public static void main(String[] args) {
        Object A = new Object();
        Object B = new Object();

        Thread t1 = new Thread(() -> {
            synchronized (A) {
                Sout.d("lock A");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (B) {
                    Sout.d("lock B");
                    Sout.d("操作");
                }
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            synchronized (B) {
                Sout.d("lock B");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (A) {
                    Sout.d("lock A");
                    Sout.d("操作");
                }
            }
        }, "t2");
        t1.start();
        t2.start();
    }
}
