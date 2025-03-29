package htd.sharedmodeltubeside.thirteen_reentrant;

import htd.utils.Sout;

import java.util.concurrent.locks.LockSupport;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-29 8:59
 * <p>
 * Desc: 同步模式之顺序控制
 * 先打印 2 再打印 1
 * park unpark 版
 */
public class TestOrder2 {
    /**
     * 2025-03-29 08:59:52.136	t2	2
     * 2025-03-29 08:59:52.136	t1	1
     */
    public static void main(String[] args) {
        /**
         * 情况1：先执行 t1，这时运行到 LockSupport.park() 发现没有干粮会等待
         * 当 t2 线程执行 LockSupport.unpark(t1) 才会继续向下执行
         *
         * 情况2：先执行 t2 。LockSupport.unpark(t1) 会先放一份干粮
         * 当执行到 t1 的 LockSupport.park() 发现有干粮，直接向下执行
         */
        Thread t1 = new Thread(() -> {
            LockSupport.park();
            Sout.d("1");
        }, "t1");

        Thread t2 = new Thread(() -> {
            Sout.d("2");
            LockSupport.unpark(t1);
        }, "t2");

        t1.start();
        t2.start();
    }
}
