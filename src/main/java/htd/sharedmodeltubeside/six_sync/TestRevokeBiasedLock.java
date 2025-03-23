package htd.sharedmodeltubeside.six_sync;

import htd.utils.Sout;
import org.openjdk.jol.info.ClassLayout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-22 22:53
 * <p>
 * 撤销偏向锁
 **/
public class TestRevokeBiasedLock {
    public static void main(String[] args) {
        // test1();
        test2();
    }

    /**
     * 撤销 - 其它线程使用对象
     * 当有其它线程使用偏向锁对象时，会将偏向锁升级为轻量级锁
     */
    private static void test1() {
        Dog d = new Dog();

        Thread t1 = new Thread(() -> {
            /**
             * 0x0000000000000005 (biasable; age: 0)
             * 偏向锁默认打开状态
             */
            Sout.d(ClassLayout.parseInstance(d).toPrintable());
            synchronized (d) {
                /**
                 * 0x000001f336b66805 (biased: 0x000000007ccdad9a; epoch: 0; age: 0)
                 * 偏向锁 偏向 t1 线程
                 */
                Sout.d(ClassLayout.parseInstance(d).toPrintable());
            }
            /**
             * 0x000001f336b66805 (biased: 0x000000007ccdad9a; epoch: 0; age: 0)
             * 锁释放，仍然偏向 t1 线程
             */
            Sout.d(ClassLayout.parseInstance(d).toPrintable());

            synchronized (TestRevokeBiasedLock.class) {
                // 通知 t2 运行
                TestRevokeBiasedLock.class.notify();
            }

            /**
             * 如果不用 wait/notify 使用 join 必须打开下面的注释
             * 因为：t1 线程不能结束，否则底层线程可能被 jvm 重用作为 t2 线程，底层线程 id 是一样的
             *
             * try {
             *     System.in.read();
             * } catch (IOException e) {
             *     e.printStackTrace();
             * }
             */

        }, "t1");
        t1.start();

        Thread t2 = new Thread(() -> {
            synchronized (TestRevokeBiasedLock.class) {
                try {
                    TestRevokeBiasedLock.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            /**
             * 0x000001f336b66805 (biased: 0x000000007ccdad9a; epoch: 0; age: 0)
             * 偏向锁，目前还是偏向 t1 线程
             */
            Sout.d(ClassLayout.parseInstance(d).toPrintable());
            synchronized (d) {
                /**
                 * 0x0000000cab6ff0e0 (thin lock: 0x0000000cab6ff0e0)
                 * 有线程竞争，偏向锁撤销，变成轻量级锁
                 */
                Sout.d(ClassLayout.parseInstance(d).toPrintable());
            }
            /**
             * 0x0000000000000001 (non-biasable; age: 0)
             * 释放后，变成无锁状态
             */
            Sout.d(ClassLayout.parseInstance(d).toPrintable());
        }, "t2");
        t2.start();
    }

    /**
     * 撤销 - wait / notify
     * 调用 wait / notify
     */
    private static void test2() {
        Dog d = new Dog();

        Thread t1 = new Thread(() -> {
            /**
             * 0x0000000000000005 (biasable; age: 0)
             * 偏向锁默认打开状态
             */
            Sout.d(ClassLayout.parseInstance(d).toPrintable());
            synchronized (d) {
                /**
                 * 0x000001f336b66805 (biased: 0x000000007ccdad9a; epoch: 0; age: 0)
                 * 偏向锁 偏向 t1 线程
                 */
                Sout.d(ClassLayout.parseInstance(d).toPrintable());
                try {
                    d.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                /**
                 * 0x000001e3a307a43a (fat lock: 0x000001e3a307a43a)
                 * 偏向锁变成重量级锁
                 */
                Sout.d(ClassLayout.parseInstance(d).toPrintable());
            }
        }, "t1");
        t1.start();

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (d) {
                Sout.d("notify");
                d.notify();
            }
        }, "t2");
        t2.start();
    }
}
