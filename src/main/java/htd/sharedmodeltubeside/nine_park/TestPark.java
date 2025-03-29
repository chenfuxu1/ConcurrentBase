package htd.sharedmodeltubeside.nine_park;

import htd.utils.Sout;

import java.util.concurrent.locks.LockSupport;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-24 22:34
 * <p>
 * Desc:
 * 特点：
 * wait，notify 和 notifyAll 必须配合 Object Monitor 一起使用，而 park，unpark 不必
 * park & unpark 是以线程为单位来【阻塞】和【唤醒】线程，而 notify 只能随机唤醒一个等待线程，notifyAll
 * 是唤醒所有等待线程，就不那么【精确】
 * park & unpark 可以先 unpark，而 wait & notify 不能先 notify
 */
public class TestPark {
    public static void main(String[] args) {
        // test1();
        test2();
    }

    /**
     * 先 park 再 unpark
     *
     * 2025-03-24 22:36:04.040	t1		start...
     * 2025-03-24 22:36:05.052	t1		park...
     * 2025-03-24 22:36:06.041	main	unpark...
     * 2025-03-24 22:36:06.041	t1		resume...
     */
    private static void test1() {
        Thread t1 = new Thread(() -> {
            Sout.d("start...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Sout.d("park...");
            LockSupport.park();
            Sout.d("resume...");
        }, "t1");
        t1.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Sout.d("unpark...");
        LockSupport.unpark(t1);
    }

    /**
     * 先 unpark 再 park
     *
     * 2025-03-24 22:37:04.341	t1		start...
     * 2025-03-24 22:37:05.334	main	unpark...
     * 2025-03-24 22:37:06.344	t1		park...
     * 2025-03-24 22:37:06.344	t1		resume...
     */
    private static void test2() {
        Thread t1 = new Thread(() -> {
            Sout.d("start...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Sout.d("park...");
            LockSupport.park();
            Sout.d("resume...");
        }, "t1");
        t1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Sout.d("unpark...");
        LockSupport.unpark(t1);
    }
}
