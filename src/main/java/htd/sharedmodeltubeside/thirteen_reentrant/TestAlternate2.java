package htd.sharedmodeltubeside.thirteen_reentrant;

import htd.utils.Sout;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-29 9:06
 * <p>
 * Desc:
 * await & signal
 * 注意：该实现没有考虑 a，b，c 线程都就绪再开始
 */
public class TestAlternate2 {
    public static void main(String[] args) {
        AwaitSignal awaitSignal = new AwaitSignal(5);
        Condition a = awaitSignal.newCondition();
        Condition b = awaitSignal.newCondition();
        Condition c = awaitSignal.newCondition();
        new Thread(() -> {
            awaitSignal.print("a", a, b);
        }).start();
        new Thread(() -> {
            awaitSignal.print("b", b, c);
        }).start();
        new Thread(() -> {
            awaitSignal.print("c", c, a);
        }).start();
        try {
            Thread.sleep(1000);
            awaitSignal.lock();
            Sout.d("开始...");
            // 唤醒 a 休息室
            a.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            awaitSignal.unlock();
        }

    }
}

class AwaitSignal extends ReentrantLock {
    private int mLoopNumber;

    public AwaitSignal(int loopNumber) {
        this.mLoopNumber = loopNumber;
    }

    /**
     * 参数1：打印内容
     * 参数2：进入哪一间休息室
     * 参数3：下一间休息室
     */
    public void print(String str, Condition current, Condition next) {
        for (int i = 0; i < mLoopNumber; i++) {
            this.lock();
            try {
                current.await();
                Sout.d(str);
                next.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.unlock();
            }
        }
    }
}
