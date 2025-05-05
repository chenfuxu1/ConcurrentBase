package htd.sharedmodeltool.thread_pool.thread_pool_executor.worker_thread;

import htd.utils.Sout;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-05-01 23:14
 **/
public class TestDeadLock2 {
    private static final String TAG = "TestDeadLock2";
    private static final List<String> MENU = Arrays.asList("地三鲜", "宫保鸡丁", "辣子鸡丁", "烤鸡翅");
    private static Random sRandom = new Random();

    /**
     * 025-05-01 23:20:16.528	pool-1-thread-1		TestDeadLock2	处理点餐...
     * 2025-05-01 23:20:16.530	pool-2-thread-1		TestDeadLock2	做菜
     * 2025-05-01 23:20:16.530	pool-1-thread-1		TestDeadLock2	上菜: 宫保鸡丁
     * 2025-05-01 23:20:16.530	pool-1-thread-1		TestDeadLock2	处理点餐...
     * 2025-05-01 23:20:16.530	pool-2-thread-1		TestDeadLock2	做菜
     * 2025-05-01 23:20:16.530	pool-1-thread-1		TestDeadLock2	上菜: 辣子鸡丁
     */
    public static void main(String[] args) {
        ExecutorService waiterPool = Executors.newFixedThreadPool(1);
        ExecutorService cookPool = Executors.newFixedThreadPool(1);
        waiterPool.execute(() -> {
            Sout.d(TAG, "处理点餐...");
            Future<String> result = cookPool.submit(() -> {
                Sout.d(TAG, "做菜");
                return cooking();
            });
            try {
                Sout.d(TAG, "上菜: " + result.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        waiterPool.execute(() -> {
            Sout.d(TAG, "处理点餐...");
            Future<String> result = cookPool.submit(() -> {
                Sout.d(TAG, "做菜");
                return cooking();
            });
            try {
                Sout.d(TAG, "上菜: " + result.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    private static String cooking() {
        return MENU.get(sRandom.nextInt(MENU.size()));
    }
}
