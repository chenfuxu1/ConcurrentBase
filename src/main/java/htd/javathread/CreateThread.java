package htd.javathread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 创建线程的方式
 */
public class CreateThread {
    public static void main(String[] args) {
        createThread();
        createThreadByRunnable();
        createThreadByFutureTask();
    }

    private static void createThread() {
        // 1.直接使用 Thread
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                System.out.println("子线程 running");
                
            }
        };
        thread1.start();
        System.out.println("主线程 running");
        // 构造方法的参数是给线程指定名字，推荐
        Thread thread2 = new Thread("thread2") {
            @Override
            // run 方法内实现了要执行的任务
            public void run() {
                System.out.println("thread2 线程运行");
            }
        };
        thread2.start();
    }

    private static void createThreadByRunnable() {
        /**
         * 2.使用 Runnable 配合 Thread
         * 创建任务对象
         */
        Runnable runnable3 = new Runnable() {
            @Override
            public void run() {
                System.out.println("runnable3 线程运行");
            }
        };
        // 参数 1 是任务对象 参数 2 是线程名字，推荐
        Thread thread3 = new Thread(runnable3, "thread3");
        thread3.start();

        // Java 8 以后可以使用 lambda 精简代码
        Runnable runnable4 = () -> {
            System.out.println("runnable4 线程运行");
        };
        Runnable runnable5 = () -> System.out.println("runnable5 线程运行");
    }

    /**
     * 方法3：futureTask
     * FutureTask 能够接收 Callable 类型的参数
     * 用来处理有返回结果的情况
     */
    private static void createThreadByFutureTask() {
        // 创建任务对象
        FutureTask<Integer> task = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println(Thread.currentThread().getName() + ": futureTask 开启的子线程");
                Thread.sleep(1000);
                return 3000;
            }
        });
        /**
         * 参数1：任务
         * 参数2：线程名称
         */
        Thread thread6 = new Thread(task, "thread6");
        thread6.start();
        try {
            Integer result = task.get(); // 主线程阻塞，同步等待 task 执行完毕的结果
            System.out.println(Thread.currentThread().getName() + "：结果是：" + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
