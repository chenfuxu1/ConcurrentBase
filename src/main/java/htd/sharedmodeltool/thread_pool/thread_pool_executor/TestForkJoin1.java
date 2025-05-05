package htd.sharedmodeltool.thread_pool.thread_pool_executor;

import htd.utils.Sout;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-05-04 9:47
 **/
public class TestForkJoin1 {
    private static final String TAG = "TestForkJoin1";

    /**
     * 2025-05-04 10:15:44.009	ForkJoinPool-1-worker-1		AddTask1	fork mN: 5 task: AddTask1{mN=4}
     * 2025-05-04 10:15:44.009	ForkJoinPool-1-worker-3		AddTask1	fork mN: 3 task: AddTask1{mN=2}
     * 2025-05-04 10:15:44.009	ForkJoinPool-1-worker-0		AddTask1	fork mN: 2 task: AddTask1{mN=1}
     * 2025-05-04 10:15:44.009	ForkJoinPool-1-worker-2		AddTask1	fork mN: 4 task: AddTask1{mN=3}
     * 2025-05-04 10:15:44.009	ForkJoinPool-1-worker-3		AddTask1	join mN: 1
     * 2025-05-04 10:15:44.009	ForkJoinPool-1-worker-0		AddTask1	join 2 + AddTask1{mN=1} = 3
     * 2025-05-04 10:15:44.010	ForkJoinPool-1-worker-3		AddTask1	join 3 + AddTask1{mN=2} = 6
     * 2025-05-04 10:15:44.010	ForkJoinPool-1-worker-2		AddTask1	join 4 + AddTask1{mN=3} = 10
     * 2025-05-04 10:15:44.010	ForkJoinPool-1-worker-1		AddTask1	join 5 + AddTask1{mN=4} = 15
     * 2025-05-04 10:15:44.010	main		TestForkJoin1	15
     * @param args
     */
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool(4);
        Sout.d(TAG, pool.invoke(new AddTask1(5)).toString());
    }
}

class AddTask1 extends RecursiveTask<Integer> {
    private static final String TAG = "AddTask1";
    private int mN;

    public AddTask1(int n) {
        mN = n;
    }

    @Override
    protected Integer compute() {
        // 如果 n 已经为 1，可以求得结果了
        if (mN == 1) {
            Sout.d(TAG, "join mN: " + mN);
            return mN;
        }
        // 将任务进行拆分 (fork)
        AddTask1 task = new AddTask1(mN - 1);
        task.fork();
        Sout.d(TAG, "fork mN: " + mN + " task: " + task);
        // 合并 (join) 结果
        int result = mN + task.join();
        Sout.d(TAG, "join " + mN + " + " + task + " = " + result);
        return result;
    }

    @Override
    public String toString() {
        return "AddTask1{" +
                "mN=" + mN +
                '}';
    }
}
