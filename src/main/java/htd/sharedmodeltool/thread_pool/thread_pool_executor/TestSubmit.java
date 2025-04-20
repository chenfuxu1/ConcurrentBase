package htd.sharedmodeltool.thread_pool.thread_pool_executor;

import htd.utils.Sout;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-20 22:18
 *
 * 提交任务 task，用返回值 Future 获得任务执行结果
 **/
public class TestSubmit {
    private static final String TAG = "TestSubmit";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // test1();

        // test2();

        test3();

        // test4();
    }

    /**
     * submit 方法
     * 提交任务 task，用返回值 Future 获得任务执行结果
     * 2025-04-20 22:22:19.344	pool-1-thread-1		TestSubmit	开始执行异步线程任务
     * 2025-04-20 22:22:20.359	main		        TestSubmit	主线程结果：路飞
     */
    private static void test1() throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        Future<String> future = pool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Sout.d(TAG,"开始执行异步线程任务");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 返回结果
                return "路飞";
            }
        });

        // 会等待子线程执行的结果返回
        Sout.d(TAG,"主线程结果：" + future.get());
    }

    /**
     * 提交 tasks 中所有任务
     * List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException;
     *
     * 2025-04-20 22:27:05.793	pool-1-thread-1		TestSubmit	开始 1...
     * 2025-04-20 22:27:05.793	pool-1-thread-2		TestSubmit	开始 2...
     * 2025-04-20 22:27:05.793	pool-1-thread-3		TestSubmit	开始 3...
     * 2025-04-20 22:27:07.802	main		        TestSubmit	结果：1
     * 2025-04-20 22:27:07.802	main		        TestSubmit	结果：2
     * 2025-04-20 22:27:07.802	main		        TestSubmit	结果：3
     */
    private static void test2() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        List<Future<String>> futures = pool.invokeAll(Arrays.asList(
                () -> {
                    Sout.d(TAG,"开始 1...");
                    Thread.sleep(1000);
                    return "1";
                },

                () -> {
                    Sout.d(TAG,"开始 2...");
                    Thread.sleep(500);
                    return "2";
                },

                () -> {
                    Sout.d(TAG,"开始 3...");
                    Thread.sleep(2000);
                    return "3";
                }

        ));

        // 遍历打印返回的集合值
        futures.forEach(f -> {
            try {
                Sout.d(TAG,"结果：" + f.get()); // 结果是按照集合的顺序输出的
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 提交 tasks 中所有任务，哪个任务先成功执行完毕，返回此任务执行结果，其它任务取消
     * <T> T invokeAny(Collection<? extends Callable<T>> tasks)
     *
     * 2025-04-20 22:28:25.671	pool-1-thread-1		TestSubmit	开始...1
     * 2025-04-20 22:28:25.671	pool-1-thread-2		TestSubmit	开始...2
     * 2025-04-20 22:28:25.671	pool-1-thread-3		TestSubmit	开始...3
     * 2025-04-20 22:28:26.179	pool-1-thread-2		TestSubmit	结束...2
     */
    private static void test3() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        String result = pool.invokeAny(Arrays.asList(
                () -> {
                    Sout.d(TAG,"开始...1");
                    Thread.sleep(1000);
                    Sout.d(TAG,"结束...1");
                    return "1";
                },

                () -> {
                    Sout.d(TAG,"开始...2");
                    Thread.sleep(500);
                    Sout.d(TAG,"结束...2");
                    return "2";
                },

                () -> {
                    Sout.d(TAG, "开始...3");
                    Thread.sleep(2000);
                    Sout.d(TAG,"结束...3");
                    return "3";
                }

        ));
        // 打印最先执行完线程的结果
        Sout.d(TAG,"result:  " + result);
    }

    private static void test4() {
    }
}

