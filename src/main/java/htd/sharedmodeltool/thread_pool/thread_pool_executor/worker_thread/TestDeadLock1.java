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
public class TestDeadLock1 {
    private static final String TAG = "TestDeadLock1";
    private static final List<String> MENU = Arrays.asList("地三鲜", "宫保鸡丁", "辣子鸡丁", "烤鸡翅");
    private static Random sRandom = new Random();


    public static void main(String[] args) {
        // test1();
        test2();
    }

    /**
     * 2025-05-01 23:26:05.135	pool-1-thread-1		TestDeadLock1	处理点餐...
     * 2025-05-01 23:26:05.136	pool-1-thread-2		TestDeadLock1	做菜
     * 2025-05-01 23:26:05.137	pool-1-thread-1		TestDeadLock1	上菜: 地三鲜
     */
    private static void test1() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() -> {
            Sout.d(TAG, "处理点餐...");
            Future<String> result = executorService.submit(() -> {
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

    /**
     * 2025-05-01 23:26:50.976	pool-1-thread-1		TestDeadLock1	处理点餐...
     * 2025-05-01 23:26:50.976	pool-1-thread-2		TestDeadLock1	处理点餐...
     */
    private static void test2() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() -> {
            Sout.d(TAG, "处理点餐...");
            Future<String> result = executorService.submit(() -> {
                Sout.d(TAG, "做菜");
                return cooking();
            });
            try {
                Sout.d(TAG, "上菜: " + result.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        executorService.execute(() -> {
            Sout.d(TAG, "处理点餐...");
            Future<String> result = executorService.submit(() -> {
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
