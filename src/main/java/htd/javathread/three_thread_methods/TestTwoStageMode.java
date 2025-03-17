package htd.javathread.three_thread_methods;

import htd.utils.Sout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-16 14:06
 * <p>
 * Desc:
 */
public class TestTwoStageMode {
    private static final String TAG = "TestTwoStageMode";

    public static void main(String[] args) {
        test1();
    }

    /**
     * 2025-03-16 14:14:37.941	Thread-0		TwoPhaseTermination	执行监控记录
     * 2025-03-16 14:14:38.950	Thread-0		TwoPhaseTermination	执行监控记录
     * 2025-03-16 14:14:39.954	Thread-0		TwoPhaseTermination	执行监控记录
     * java.lang.InterruptedException: sleep interrupted
     * 	at java.lang.Thread.sleep(Native Method)
     * 	at htd.javathread.three_thread_methods.TwoPhaseTermination.lambda$start$0(TestTwoStageMode.java:47)
     * 	at java.lang.Thread.run(Thread.java:748)
     * 2025-03-16 14:14:40.433	Thread-0		TwoPhaseTermination	料理后事
     */
    private static void test1() {
        TwoPhaseTermination twoPhaseTermination = new TwoPhaseTermination();
        twoPhaseTermination.start();

        try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        twoPhaseTermination.stop();
    }
}

class TwoPhaseTermination {
    private static final String TAG = "TwoPhaseTermination";
    private Thread mMonitor;

    // 启动监控线程
    public void start() {
        mMonitor = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                if (current.isInterrupted()) {
                    Sout.d(TAG, "料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000); // 情况 1，睡眠时被打断
                    Sout.d(TAG, "执行监控记录"); // 情况 2，执行正常代码时被打断，对于这种情况，下次循环就可正常退出
                } catch (InterruptedException e) {
                    // 情况 1，会捕获异常，且会将打断状态置位，所以这里再次将其设置为 true，那么下次循环就会正常退出
                    current.interrupt();
                    e.printStackTrace();
                }
            }
        });
        mMonitor.start();
    }

    // 停止监控线程
    public void stop() {
        mMonitor.interrupt();
    }
}
