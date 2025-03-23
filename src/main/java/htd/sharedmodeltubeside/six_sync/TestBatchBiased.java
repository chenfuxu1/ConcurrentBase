package htd.sharedmodeltubeside.six_sync;

import htd.utils.Sout;
import org.openjdk.jol.info.ClassLayout;

import java.util.Vector;
import java.util.concurrent.locks.LockSupport;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-23 11:56
 * <p>
 * 测试批量重偏向
 **/
public class TestBatchBiased {
    private static Thread t1, t2, t3 = null;

    public static void main(String[] args) throws InterruptedException {
        // test1();
        test2();
    }

    // 批量重偏向
    private static void test1() throws InterruptedException {
        Vector<Dog> list = new Vector<>();
        /**
         * t1 线程中，各个对象都属于偏向锁状态，偏向 t1 线程
         *
         * 0 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 1 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 2 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 3 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 4 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 5 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 6 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 7 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 8 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 9 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 10 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 11 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 12 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 13 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 14 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 15 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 16 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 17 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 18 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 19 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 20 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 21 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 22 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 23 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 24 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 25 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 26 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 27 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 28 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         * 29 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0)
         */
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                Dog d = new Dog();
                list.add(d);
                synchronized (d) {
                    Sout.d(i + "\t" + ClassLayout.parseInstance(d).toPrintable());
                }
            }
            synchronized (list) {
                list.notify();
            }
        }, "t1");
        t1.start();

        Thread t2 = new Thread(() -> {
            synchronized (list) {
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Sout.d("===============> ");
            /**
             * 0 - 18
             * 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0) 偏向 t1 线程
             * 0x000000ec5deff300 (thin lock: 0x000000ec5deff300) 升级为轻量级锁
             * 0x0000000000000001 (non-biasable; age: 0) 用完之后，变成无锁状态
             *
             * 当撤销偏向锁阈值超过 20 次后，jvm 会这样觉得，我是不是偏向错了呢，于是会在给这些对象加锁时重新偏向至加锁线程
             *
             * 19 - 29
             * 0x0000023fd562d805 (biased: 0x000000008ff558b6; epoch: 0; age: 0) 偏向 t1 线程
             * 0x0000023fd562e905 (biased: 0x000000008ff558ba; epoch: 0; age: 0) 偏向 t2 线程
             * 0x0000023fd562e905 (biased: 0x000000008ff558ba; epoch: 0; age: 0) 偏向 t2 线程
             *
             */
            for (int i = 0; i < 30; i++) {
                Dog d = list.get(i);
                Sout.d(i + "\t" + ClassLayout.parseInstance(d).toPrintable());
                synchronized (d) {
                    Sout.d(i + "\t" + ClassLayout.parseInstance(d).toPrintable());
                }
                Sout.d(i + "\t" + ClassLayout.parseInstance(d).toPrintable());
            }
        }, "t2");
        t2.start();
    }

    /**
     * 测试批量撤销
     */
    private static void test2() throws InterruptedException {
        Vector<Dog> list = new Vector<>();
        int loopNumber = 39;
        /**
         * 0 - 38
         * 0x0000027aa7ef4005 (biased: 0x000000009ea9fbd0; epoch: 0; age: 0)
         *
         * 先让这 39 个对象都偏向 t1 线程
         */
        t1 = new Thread(() -> {
            for (int i = 0; i < loopNumber; i++) {
                Dog d = new Dog();
                list.add(d);
                synchronized (d) {
                    Sout.d(i + "\t" + ClassLayout.parseInstance(d).toPrintable());
                }
            }
            LockSupport.unpark(t2);
        }, "t1");
        t1.start();

        t2 = new Thread(() -> {
            LockSupport.park();
            Sout.d("===============> ");
            /**
             * 0 - 18
             * 0x0000027aa7ef4005 (biased: 0x000000009ea9fbd0; epoch: 0; age: 0) 偏向 t1 线程
             * 0x000000f2975ff328 (thin lock: 0x000000f2975ff328) 升级为轻量级锁
             * 0x0000000000000001 (non-biasable; age: 0) 变为无锁状态
             *
             * 19 - 38
             * 0x0000027aa7ef4005 (biased: 0x000000009ea9fbd0; epoch: 0; age: 0) 偏向 t1 线程
             * 0x0000027aa7ed6905 (biased: 0x000000009ea9fb5a; epoch: 0; age: 0) 偏向 t2 线程
             * 0x0000027aa7ed6905 (biased: 0x000000009ea9fb5a; epoch: 0; age: 0) 偏向 t3 线程
             */
            for (int i = 0; i < loopNumber; i++) {
                Dog d = list.get(i);
                Sout.d(i + "\t" + ClassLayout.parseInstance(d).toPrintable());
                synchronized (d) {
                    Sout.d(i + "\t" + ClassLayout.parseInstance(d).toPrintable());
                }
                Sout.d(i + "\t" + ClassLayout.parseInstance(d).toPrintable());
            }
            LockSupport.unpark(t3);
        }, "t2");
        t2.start();

        t3 = new Thread(() -> {
            LockSupport.park();
            Sout.d("===============> ");
            /**
             * 0 - 18
             * 0x0000000000000001 (non-biasable; age: 0) 无锁状态
             * 0x000000f2976fef38 (thin lock: 0x000000f2976fef38) 轻量级锁
             * 0x0000000000000001 (non-biasable; age: 0) 变为无锁状态
             *
             * 19 - 38
             * 0x0000027aa7ed6905 (biased: 0x000000009ea9fb5a; epoch: 0; age: 0) 偏向 t2 线程
             * 0x000000f2976fef38 (thin lock: 0x000000f2976fef38) 升级为轻量级锁
             * 0x0000000000000001 (non-biasable; age: 0) 变为无锁状态
             */
            for (int i = 0; i < loopNumber; i++) {
                Dog d = list.get(i);
                Sout.d(i + "\t" + ClassLayout.parseInstance(d).toPrintable());
                synchronized (d) {
                    Sout.d(i + "\t" + ClassLayout.parseInstance(d).toPrintable());
                }
                Sout.d(i + "\t" + ClassLayout.parseInstance(d).toPrintable());
            }
        }, "t3");
        t3.start();

        t3.join();
        // 0x0000000000000001 (non-biasable; age: 0) 无锁，已经撤销 39 次，第四十次就是不可偏向的对象了
        Sout.d(ClassLayout.parseInstance(new Dog()).toPrintable());
    }
}


