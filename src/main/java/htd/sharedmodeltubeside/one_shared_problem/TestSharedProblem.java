package htd.sharedmodeltubeside.one_shared_problem;


import htd.utils.Sout;

/**
 * Project: Concurrent
 * Create By: ChenFuXu
 * DateTime: 2022/9/4 19:59
 **/
public class TestSharedProblem {
    private static int sCount = 0;
    private static final Object mRoom = new Object();

    public static void main(String[] args) {
        // test1();

        test2();
    }

    /**
     * 两个线程对初始值为 0 的静态变量一个做自增，一个做自减，
     * 各做 5000 次，结果是 0 吗？
     */
    private static void test1() {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                sCount++;
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                sCount--;
            }
        }, "t2");

        try {
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            Sout.d("sCount: " + sCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 应用互斥
     * 使用阻塞式的解决方案：synchronized，来解决上述问题，即俗称的【对象锁】，它采用互斥的方式让同一
     * 时刻至多只有一个线程能持有【对象锁】，其它线程再想获取这个【对象锁】时就会阻塞住。这样就能保证拥有锁
     * 的线程可以安全的执行临界区内的代码，不用担心线程上下文切换
     */
    private static void test2() {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (mRoom) {
                    sCount++;
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (mRoom) {
                    sCount--;
                }
            }
        }, "t2");

        try {
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            Sout.d("sCount: " + sCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
