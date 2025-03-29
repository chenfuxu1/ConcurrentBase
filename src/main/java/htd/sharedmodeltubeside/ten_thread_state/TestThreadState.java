package htd.sharedmodeltubeside.ten_thread_state;

import htd.utils.Sout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-24 22:56
 * <p>
 * Desc:
 *
 * 情况2：RUNNABLE <--> WAITING
 * t 线程用 synchronized(obj) 获取了对象锁后
 * 调用 obj.wait() 方法时，t 线程从 RUNNABLE --> WAITING
 * 调用 obj.notify() ， obj.notifyAll() ， t.interrupt() 时
 *     竞争锁成功，t 线程从 WAITING --> RUNNABLE
 *     竞争锁失败，t 线程从 WAITING --> BLOCKED
 */
public class TestThreadState {
    private static final Object OBJ = new Object();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (OBJ) {
                Sout.d("t1 线程执行开始");
                try {
                    OBJ.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Sout.d("t1 线程执行结束"); // 断点位置
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (OBJ) {
                Sout.d("t2 线程执行开始");
                try {
                    OBJ.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Sout.d("t2 线程执行结束"); // 断点位置
            }
        }, "t2").start();

        Thread.sleep(1000);
        Sout.d("开始唤醒 t1 或 t2 线程");
        synchronized (OBJ) {
            // 唤醒 OBJ 上等待的所有线程
            OBJ.notifyAll(); // 断点位置
        }
    }
}
