package htd.sharedmodelmemory.one_visible;

import htd.utils.Sout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-29 14:00
 * <p>
 * Desc:
 */
public class TestBalking {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination3 twoPhaseTermination = new TwoPhaseTermination3();
        // 调用该方法多次
        twoPhaseTermination.start();
        twoPhaseTermination.start();
        // 3s 后打断
        Thread.sleep(3000);
        twoPhaseTermination.stop();
    }
}

// 监控线程类
class TwoPhaseTermination3 {
    private Thread mMonitor;

    /**
     * 因为 mStop 是多个线程共享的变量，需要保证可见性
     * 否则有可能主线程改变的，子线程获取的还是缓存的结果，导致无法正确退出
     * 因此这里需要加 volatile
     */
    private volatile boolean mStop = false;

    /**
     * 判断是否执行过 start 方法
     */
    private boolean mStarting = false;

    // 启动监控线程
    public void start() {
        /**
         * 这里线程 1，线程 2 都运行到此处，由于还没执行 mStarting = true
         * 还是会创建多个监控线程
         * 如果给 mStarting 加 volatile 字段能解决上述问题吗？不能
         * 因为加了 volatile 只能保证可见性，只能保证线程 1 2 读到的值是当前值
         * 但没有保证原子性
         * 所以需要给整段代码加锁 synchronized
         */
        synchronized (this) {
            if (mStarting) {
                // 已经调用过，直接返回
                return;
            }
            mStarting = true;
        }
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
