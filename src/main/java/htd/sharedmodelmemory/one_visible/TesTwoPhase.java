package htd.sharedmodelmemory.one_visible;

import htd.utils.Sout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-29 13:53
 * <p>
 * Desc: 两阶段终止模式
 */
public class TesTwoPhase {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination twoPhaseTermination = new TwoPhaseTermination();
        twoPhaseTermination.start();
        // 3s 后打断
        Thread.sleep(3000);
        twoPhaseTermination.stop();
    }
}

// 监控线程类
class TwoPhaseTermination {
    private Thread mMonitor;

    /**
     * 因为 mStop 是多个线程共享的变量，需要保证可见性
     * 否则有可能主线程改变的，子线程获取的还是缓存的结果，导致无法正确退出
     * 因此这里需要加 volatile
     */
    private volatile boolean mStop = false;

    // 启动监控线程
    public void start() {
        mMonitor = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                if (mStop) {
                    Sout.d("料理后事");
                    Sout.d("结束循环");
                    break;
                }
                try {
                    // 未设置打断标志位，正常休眠 2s
                    Sout.d("休眠 2s");
                    Thread.sleep(2000);
                    Sout.d("执行监控记录");
                } catch (InterruptedException e) {
                }
            }
        }, "monitor");
        mMonitor.start();
    }

    // 停止监控线程
    public void stop() {
        mStop = true;
        mMonitor.interrupt();
    }
}
