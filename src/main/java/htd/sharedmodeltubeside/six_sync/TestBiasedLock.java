package htd.sharedmodeltubeside.six_sync;

import htd.utils.Sout;
import org.openjdk.jol.info.ClassLayout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-22 22:03
 * <p>
 * 偏向锁
 **/
public class TestBiasedLock {
    public static void main(String[] args) throws InterruptedException {
        // test1();
        // test2();
        // test3();
        // test4();
        test5();
    }

    // 1.测试延迟性
    private static void test1() throws InterruptedException {
        /**
         * 偏向锁是默认是延迟的，不会在程序启动时立即生效
         *
         * 可以看到，默认最后三位是 001 表示还没开启，可以延迟四五秒后再看
         * 0x0000000000000001 (non-biasable; age: 0)
         */
        Dog dog = new Dog();
        Sout.d(ClassLayout.parseInstance(dog).toPrintable());

        Thread.sleep(4000);
        Dog dog2 = new Dog();
        /**
         * 4 s 后输出 0x0000000000000005 (biasable; age: 0)
         * 后三位是 101，表明偏向锁已经开启了
         */
        Sout.d(ClassLayout.parseInstance(dog2).toPrintable());
    }

    /**
     * 2.测试延迟性, 偏向锁是默认是延迟的
     * 通过 -XX:BiasedLockingStartupDelay=0 关闭延时
     */
    private static void test2() throws InterruptedException {
        /**
         * 可以看到，后三位立即就输出了 101
         * 0x0000000000000005 (biasable; age: 0)
         */
        Dog dog = new Dog();
        Sout.d(ClassLayout.parseInstance(dog).toPrintable());
    }

    /**
     * 3.测试偏向锁
     * 通过 -XX:BiasedLockingStartupDelay=0 关闭延时
     */
    private static void test3() throws InterruptedException {
        /**
         * 可以看到，后三位立即就输出了 101, 默认偏向锁
         * 0x0000000000000005 (biasable; age: 0)
         */
        Dog dog = new Dog();
        Sout.d(ClassLayout.parseInstance(dog).toPrintable());

        // 加锁时
        synchronized (dog) {
            // 0x00000297847a7005 (biased: 0x00000000a5e11e9c; epoch: 0; age: 0)
            // 5 前面是 线程 id
            Sout.d(ClassLayout.parseInstance(dog).toPrintable());
        }

        // 0x00000297847a7005 (biased: 0x00000000a5e11e9c; epoch: 0; age: 0)
        // 即使 偏向锁 释放了，可以看到，还是这个 id，偏向这个线程的锁
        // 处于偏向锁的对象解锁后，线程 id 仍存储于对象头中
        Sout.d(ClassLayout.parseInstance(dog).toPrintable());
    }

    /**
     * 4.测试禁用偏向锁
     * 通过 -XX:-UseBiasedLocking 禁用
     */
    private static void test4() throws InterruptedException {
        /**
         * 默认是 01，正常无锁状态
         * 0x0000000000000001 (non-biasable; age: 0)
         */
        Dog dog = new Dog();
        Sout.d(ClassLayout.parseInstance(dog).toPrintable());

        // 加锁时
        synchronized (dog) {
            // 0x0000005369aff348 (thin lock: 0x0000005369aff348)
            // 属于轻量级锁
            Sout.d(ClassLayout.parseInstance(dog).toPrintable());
        }

        // 0x0000000000000001 (non-biasable; age: 0)
        // 锁释放，又属于无锁状态
        Sout.d(ClassLayout.parseInstance(dog).toPrintable());
    }

    /**
     * 5.测试 HashCode
     * 通过 -XX:BiasedLockingStartupDelay=0 关闭延时
     */
    private static void test5() throws InterruptedException {
        /**
         * 已经关闭延时，默认是开启偏向锁的
         */
        Dog dog = new Dog();
        // 调用对象的 hashCode 会禁用偏向锁
        dog.hashCode();
        /**
         * 可以看到，偏向锁已经被禁用了
         * 0x00000015db974201 (hash: 0x15db9742; age: 0)
         */
        Sout.d(ClassLayout.parseInstance(dog).toPrintable());

        // 加锁时
        synchronized (dog) {
            // 0x000000d166fff2b8 (thin lock: 0x000000d166fff2b8)
            // 属于轻量级锁
            Sout.d(ClassLayout.parseInstance(dog).toPrintable());
        }

        // 0x00000015db974201 (hash: 0x15db9742; age: 0)
        // 锁释放，又属于无锁状态
        Sout.d(ClassLayout.parseInstance(dog).toPrintable());
    }

}

class Dog {
}
