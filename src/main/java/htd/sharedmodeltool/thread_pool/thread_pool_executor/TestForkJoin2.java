package htd.sharedmodeltool.thread_pool.thread_pool_executor;

import htd.utils.Sout;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-05-04 9:47
 **/
public class TestForkJoin2 {
    private static final String TAG = "TestForkJoin2";

    /**
     * 2025-05-04 11:07:57.158	ForkJoinPool-1-worker-2		AddTask2	fork() AddTask2{mBegin=1, mEnd=2} + AddTask2{mBegin=3, mEnd=3} = ?
     * 2025-05-04 11:07:57.158	ForkJoinPool-1-worker-1		AddTask2	fork() AddTask2{mBegin=1, mEnd=3} + AddTask2{mBegin=4, mEnd=5} = ?
     * 2025-05-04 11:07:57.158	ForkJoinPool-1-worker-0		AddTask2	join mBegin: 1 mEnd: 2
     * 2025-05-04 11:07:57.158	ForkJoinPool-1-worker-3		AddTask2	join mBegin: 4 mEnd: 5
     * 2025-05-04 11:07:57.159	ForkJoinPool-1-worker-1		AddTask2	join mBegin: 3
     * 2025-05-04 11:07:57.159	ForkJoinPool-1-worker-2		AddTask2	join() AddTask2{mBegin=1, mEnd=2} + AddTask2{mBegin=3, mEnd=3} = 6
     * 2025-05-04 11:07:57.159	ForkJoinPool-1-worker-1		AddTask2	join() AddTask2{mBegin=1, mEnd=3} + AddTask2{mBegin=4, mEnd=5} = 15
     * 2025-05-04 11:07:57.159	main		TestForkJoin2	15
     * @param args
     */
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool(4);
        Sout.d(TAG, pool.invoke(new AddTask2(1, 5)).toString());
    }
}

class AddTask2 extends RecursiveTask<Integer> {
    private static final String TAG = "AddTask2";

    private int mBegin;
    private int mEnd;

    public AddTask2(int begin, int end) {
        mBegin = begin;
        mEnd = end;
    }

    @Override
    protected Integer compute() {
        // 5, 5
        if (mBegin == mEnd) {
            Sout.d(TAG, "join mBegin: " + mBegin);
            return mBegin;
        }
        // 4, 5
        if (mEnd - mBegin == 1) {
            Sout.d(TAG, "join mBegin: " + mBegin + " mEnd: " + mEnd);
            return mEnd + mBegin;
        }
        // 1 5
        int mid = (mEnd + mBegin) / 2; // 3
        AddTask2 task1 = new AddTask2(mBegin, mid); // 1, 3
        task1.fork();
        AddTask2 task2 = new AddTask2(mid + 1, mEnd); // 4, 5
        task2.fork();
        Sout.d(TAG, "fork() " + task1 + " + " + task2 + " = ?");
        int result = task1.join() + task2.join();
        Sout.d(TAG, "join() " + task1 + " + " + task2 + " = " + result);
        return result;
    }

    @Override
    public String toString() {
        return "AddTask2{" +
                "mBegin=" + mBegin +
                ", mEnd=" + mEnd +
                '}';
    }
}
