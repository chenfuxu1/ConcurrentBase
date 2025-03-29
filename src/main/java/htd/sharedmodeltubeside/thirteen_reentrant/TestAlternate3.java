package htd.sharedmodeltubeside.thirteen_reentrant;

import htd.utils.Sout;

import java.util.concurrent.locks.LockSupport;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-29 9:11
 * <p>
 * Desc:
 * 交替输出
 * Park unPark 版
 */
public class TestAlternate3 {
    public static void main(String[] args) {
        SyncPark syncPark = new SyncPark(5);
        Thread t1 = new Thread(() -> {
            syncPark.print("a");
        }, "t1");
        Thread t2 = new Thread(() -> {
            syncPark.print("b");
        }, "t2");
        Thread t3 = new Thread(() -> {
            syncPark.print("c");
        }, "t3");
        syncPark.setThreads(t1, t2, t3);
        syncPark.start();

    }
}

class SyncPark {
    private final int mLoopNumber;
    private Thread[] mThreads;

    public SyncPark(int loopNumber) {
        this.mLoopNumber = loopNumber;
    }

    public void setThreads(Thread... threads) {
        this.mThreads = threads;
    }

    public void print(String str) {
        for (int i = 0; i < mLoopNumber; i++) {
            LockSupport.park();
            Sout.d(str);
            LockSupport.unpark(nextThread());
        }
    }

    private Thread nextThread() {
        Thread current = Thread.currentThread();
        int index = 0;
        for (int i = 0; i < mThreads.length; i++) {
            if (mThreads[i] == current) {
                index = i;
                break;
            }
        }
        if (index < mThreads.length - 1) {
            return mThreads[index + 1];
        } else {
            return mThreads[0];
        }
    }

    public void start() {
        for (Thread thread : mThreads) {
            thread.start();
        }
        LockSupport.unpark(mThreads[0]);
    }
}
