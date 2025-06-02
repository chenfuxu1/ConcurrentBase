package htd.sharedmodeltool.juc.countdownlatch;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-06-01 16:21
 *
 * 应用-等待多线程准备完毕
 **/
public class Test3 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        Random random = new Random();
        String[] all = new String[10];
        for (int j = 0; j < 10; j++) {
            int k = j;
            service.submit(() -> {
                for (int i = 0; i <= 100; i++) {
                    try {
                        Thread.sleep(random.nextInt(150));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    all[k] = i + "%";
                    System.out.print("\r" + Arrays.toString(all));
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        System.out.println("\n游戏开始...");
        service.shutdown(); // 关闭线程池
    }
}
