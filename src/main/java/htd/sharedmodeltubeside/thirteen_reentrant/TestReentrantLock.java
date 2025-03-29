package htd.sharedmodeltubeside.thirteen_reentrant;

import htd.utils.Sout;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-29 7:35
 * <p>
 * Desc: ReentrantLock
 * 可重入性
 * 可重入是指同一个线程如果首次获得了这把锁，那么因为它是这把锁的拥有者，因此有权利再次获取这把锁
 * 如果是不可重入锁，那么第二次获得锁时，自己也会被锁挡住
 */
public class TestReentrantLock {
    private static ReentrantLock sLock = new ReentrantLock();

    /**
     * 2025-03-29 07:37:32.995	main	执行 test1
     * 2025-03-29 07:37:32.996	main	执行 test2
     * 2025-03-29 07:37:32.996	main	执行 test3
     */
    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        sLock.lock();
        try {
            Sout.d("执行 test1");
            test2();
        } finally {
            sLock.unlock();
        }
    }

    private static void test2() {
        sLock.lock();
        try {
            Sout.d("执行 test2");
            test3();
        } finally {
            sLock.unlock();
        }
    }

    private static void test3() {
        sLock.lock();
        try {
            Sout.d("执行 test3");
        } finally {
            sLock.unlock();
        }
    }
}
