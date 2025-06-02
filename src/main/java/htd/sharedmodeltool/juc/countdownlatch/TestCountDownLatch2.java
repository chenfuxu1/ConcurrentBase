package htd.sharedmodeltool.juc.countdownlatch;

import htd.utils.Sout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-06-01 16:17
 *
 * 使用线程池改进
 *
 **/
public class TestCountDownLatch2 {
    /**
     * 2025-06-01 16:19:23.815	pool-1-thread-1		t1 begin...
     * 2025-06-01 16:19:23.815	pool-1-thread-4		等待开始...
     * 2025-06-01 16:19:23.815	pool-1-thread-3		t3 begin...
     * 2025-06-01 16:19:23.815	pool-1-thread-2		t2 begin...
     * 2025-06-01 16:19:24.830	pool-1-thread-1		t1 end... countDownLatch.getCount() = 2
     * 2025-06-01 16:19:25.827	pool-1-thread-2		t2 end... countDownLatch.getCount() = 1
     * 2025-06-01 16:19:26.823	pool-1-thread-4		运行结束...
     * 2025-06-01 16:19:26.823	pool-1-thread-3		t3 end... countDownLatch.getCount() = 0
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        ExecutorService service = Executors.newFixedThreadPool(4);
        service.submit(() -> {
            Sout.d("t1 begin...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
            Sout.d("t1 end... countDownLatch.getCount() = " + countDownLatch.getCount());
        });

        service.submit(() -> {
            Sout.d("t2 begin...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
            Sout.d("t2 end... countDownLatch.getCount() = " + countDownLatch.getCount());
        });

        service.submit(() -> {
            Sout.d("t3 begin...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
            Sout.d("t3 end... countDownLatch.getCount() = " + countDownLatch.getCount());
        });

        service.submit(() -> {
            Sout.d("等待开始...");
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Sout.d("运行结束...");
        });

    }
}

