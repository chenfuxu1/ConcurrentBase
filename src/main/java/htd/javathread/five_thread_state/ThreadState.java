package htd.javathread.five_thread_state;

import htd.utils.Sout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-19 0:06
 * <p>
 * Desc:
 */
public class ThreadState {
    private static final String TAG = "ThreadState";

    public static void main(String[] args) throws Exception {
        test1();
    }

    /**
     * 2025-03-19 00:19:37.213	main	ThreadState	t1.getState(): NEW
     * 2025-03-19 00:19:37.214	main	ThreadState	t2.getState(): RUNNABLE
     * 2025-03-19 00:19:37.214	t3		ThreadState	t3 running end
     * 2025-03-19 00:19:37.427	main	ThreadState	t3.getState(): TERMINATED
     * 2025-03-19 00:19:37.630	main	ThreadState	t4.getState(): TIMED_WAITING
     * 2025-03-19 00:19:37.833	main	ThreadState	t5.getState(): WAITING
     * 2025-03-19 00:19:38.035	main	ThreadState	t6.getState(): BLOCKED
     */
    private static void test1() throws InterruptedException {
        // 1.第一种，刚 new 出来，属于 NEW
        Thread t1 = new Thread(() -> {});
        t1.setName("t1");
        Sout.d(TAG, "t1.getState(): " + t1.getState());

        // 2.第二种，运行中的线程，属于 RUNNABLE
        Thread t2 = new Thread(() -> {
            synchronized (ThreadState.class) {
                while (true) {

                }
            }
        });
        t2.setName("t2");
        t2.start();
        Sout.d(TAG, "t2.getState(): " + t2.getState());

        // 3.线程正常执行完了，处于结束 TERMINATED
        Thread t3 = new Thread(() -> {
            Sout.d(TAG, "t3 running end");
        });
        t3.setName("t3");
        t3.start();
        Thread.sleep(200);
        Sout.d(TAG, "t3.getState(): " + t3.getState());

        // 4.第四种，处于有时间的等待状态 TIMED_WAITING
        Thread t4 = new Thread(() -> {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t4.setName("t4");
        t4.start();
        Thread.sleep(200);
        Sout.d(TAG, "t4.getState(): " + t4.getState());

        // 5.第五种，不知会等多久 WAITING 状态
        Thread t5 = new Thread(() -> {
            try {
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t5.setName("t5");
        t5.start();
        Thread.sleep(200);
        Sout.d(TAG, "t5.getState(): " + t5.getState());

        // 6.第六种，如果未获取到锁，将处于阻塞 BLOCKED 的状态
        Thread t6 = new Thread(() -> {
            // 因为 t2 会先获取到锁，这里获取不到锁，就会处于 BLOCKED
            synchronized (ThreadState.class) {
                try {
                    Thread.sleep(50000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t6.setName("t6");
        t6.start();
        Thread.sleep(200);
        Sout.d(TAG, "t6.getState(): " + t6.getState());
    }
}
