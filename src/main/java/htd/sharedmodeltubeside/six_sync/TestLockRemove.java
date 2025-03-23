package htd.sharedmodeltubeside.six_sync;


import htd.utils.Sout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-23 14:05
 * <p>
 * 测试锁消除
 **/
public class TestLockRemove {
    private static final String TAG = "TestLockRemove";

    static int sX = 0;

    public static void main(String[] args) {
        test1();
    }

    /**
     * JIT 即时编译器优化，判断局部变量没有共享，会将锁优化掉，锁消除，默认开关是打开的
     * -XX:-EliminateLocks 会关闭锁消除
     * 2025-03-23 14:11:28.050	main	TestLockRemove	method a cost time: 5 ms
     * 2025-03-23 14:11:28.550	main	TestLockRemove	method b cost time: 498 ms
     */
    private static void test1() {
        a();
        b();
    }

    private static void a() {
        sX = 0;
        long start = System.currentTimeMillis();

        for (int i = 0; i < 100_000_000; i++) {
            sX++;
        }

        Sout.d(TAG, "method a cost time: " + (System.currentTimeMillis() - start) + " ms");
    }

    private static void b() {
        sX = 0;
        long start = System.currentTimeMillis();
        Object object = new Object();

        for (int i = 0; i < 100_000_000; i++) {
            synchronized (object) {
                sX++;
            }
        }

        Sout.d(TAG, "method b cost time: " + (System.currentTimeMillis() - start) + " ms");
    }
}
