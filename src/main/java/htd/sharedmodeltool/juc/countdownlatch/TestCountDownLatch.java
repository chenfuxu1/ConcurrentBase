package htd.sharedmodeltool.juc.countdownlatch;

import htd.utils.Sout;

import java.util.concurrent.CountDownLatch;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-06-01 16:12
 **/
public class TestCountDownLatch {
    /**
     * 2025-06-01 16:14:03.608	t2		t2 begin...
     * 2025-06-01 16:14:03.608	t3		t3 begin...
     * 2025-06-01 16:14:03.608	main		主线程等待...
     * 2025-06-01 16:14:03.608	t1		t1 begin...
     * 2025-06-01 16:14:04.624	t1		t1 end... countDownLatch.getCount() = 2
     * 2025-06-01 16:14:05.621	t2		t2 end... countDownLatch.getCount() = 1
     * 2025-06-01 16:14:06.615	t3		t3 end... countDownLatch.getCount() = 0
     * 2025-06-01 16:14:06.615	main		主线程运行结束...
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);

        new Thread(() -> {
            Sout.d("t1 begin...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
            Sout.d("t1 end... countDownLatch.getCount() = " + countDownLatch.getCount());
        }, "t1").start();

        new Thread(() -> {
            Sout.d("t2 begin...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
            Sout.d("t2 end... countDownLatch.getCount() = " + countDownLatch.getCount());
        }, "t2").start();

        new Thread(() -> {
            Sout.d("t3 begin...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
            Sout.d("t3 end... countDownLatch.getCount() = " + countDownLatch.getCount());
        }, "t3").start();

        Sout.d("主线程等待...");
        countDownLatch.await();
        Sout.d("主线程运行结束...");
    }
}
