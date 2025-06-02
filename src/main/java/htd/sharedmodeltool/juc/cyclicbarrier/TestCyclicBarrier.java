package htd.sharedmodeltool.juc.cyclicbarrier;

import htd.utils.Sout;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-06-01 16:44
 *
 * CyclicBarrier
 * 循环栅栏，用来进行线程协作，等待线程满足某个计数。构造时设置『计数个数』，每个线程执
 * 行到某个需要“同步”的时刻调用 await() 方法进行等待，计数个数会减 1，当减到 0 时，不会
 * 阻塞了，会直接向下运行
 *
 * 与 CountDownLatch 不同之处在于：CountDownLatch 个数不会被修改，只能一直递减，直至为 0 结束
 * 而 CyclicBarrier 计数个数减为 0 ，运行结束，计数个数重置为初始值
 **/
public class TestCyclicBarrier {
    /**
     * 2025-06-01 16:47:27.438	pool-1-thread-1		task1 begin...
     * 2025-06-01 16:47:27.438	pool-1-thread-2		task2 begin...
     * 2025-06-01 16:47:29.449	pool-1-thread-2		task1 task2 end...
     * 2025-06-01 16:47:29.449	pool-1-thread-2		task2 end...
     * 2025-06-01 16:47:29.449	pool-1-thread-1		task1 end...
     * 2025-06-01 16:47:29.449	pool-1-thread-2		task1 begin...
     * 2025-06-01 16:47:29.449	pool-1-thread-1		task2 begin...
     * 2025-06-01 16:47:31.458	pool-1-thread-1		task1 task2 end...
     * 2025-06-01 16:47:31.458	pool-1-thread-1		task2 end...
     * 2025-06-01 16:47:31.458	pool-1-thread-2		task1 end...
     * 2025-06-01 16:47:31.458	pool-1-thread-1		task1 begin...
     * 2025-06-01 16:47:31.458	pool-1-thread-2		task2 begin...
     * 2025-06-01 16:47:33.469	pool-1-thread-2		task1 task2 end...
     * 2025-06-01 16:47:33.469	pool-1-thread-2		task2 end...
     * 2025-06-01 16:47:33.469	pool-1-thread-1		task1 end...
     */
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(2);
        /**
         * 参数1：计数个数
         * 参数2：当所有任务执行完毕后会执行
         */
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> {
            Sout.d("task1 task2 end...");
        });

        for (int i = 0; i < 3; i++) {
            service.submit(() -> {
                Sout.d("task1 begin...");
                try {
                    Thread.sleep(1000); // 计数个数减 1
                    cyclicBarrier.await();
                    Sout.d("task1 end...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });

            service.submit(() -> {
                Sout.d("task2 begin...");
                try {
                    Thread.sleep(2000); // 计数个数减 1
                    cyclicBarrier.await();
                    Sout.d("task2 end...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }

        service.shutdown();
    }
}

