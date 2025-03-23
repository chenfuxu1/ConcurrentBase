package htd.sharedmodeltubeside.four_thread_safe;

import java.util.ArrayList;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-22 11:56
 *
 * 线程安全分析
 **/
public class TestThreadSafe2 {
    private static final int THREAD_NUMBER = 2;
    private static final int LOOP_NUMBER = 200;

    public static void main(String[] args) {
        ThreadSafe threadSafe = new ThreadSafe();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> {
                threadSafe.method1(LOOP_NUMBER);
            }, "Thread" + (i + 1)).start();
        }
    }
}

class ThreadSafe {

    public void method1(int loopNumber) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < loopNumber; i++) {
            // 临界区，会产生竟态条件
            method2(list);

            method3(list);
        }
    }

    private void method3(ArrayList<String> list) {
        list.remove(0);
    }

    private void method2(ArrayList<String> list) {
        list.add("1");
    }
}
