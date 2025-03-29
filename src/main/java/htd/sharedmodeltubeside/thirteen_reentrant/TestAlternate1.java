package htd.sharedmodeltubeside.thirteen_reentrant;

import htd.utils.Sout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-29 9:03
 * <p>
 * Desc: 要求：三个线程顺序输出 abc abc abc...
 * 使用 wait notify
 * 输出内容     等待标记    下一个标记
 * a            1           2
 * b            2           3
 * c            3           1
 */
public class TestAlternate1 {
    public static void main(String[] args) {
        WaitNotify waitNotify = new WaitNotify(1, 5);
        Thread t1 = new Thread(() -> {
            waitNotify.print("a", 1, 2);
        }, "t1");
        Thread t2 = new Thread(() -> {
            waitNotify.print("b", 2, 3);
        }, "t2");
        Thread t3 = new Thread(() -> {
            waitNotify.print("c", 3, 1);
        }, "t3");
        t1.start();
        t2.start();
        t3.start();
    }

}

class WaitNotify {
    // 等待标记
    private int mFlag;
    // 循环次数
    private int mLoopNumber;

    public WaitNotify(int flag, int loopNumber) {
        mFlag = flag;
        mLoopNumber = loopNumber;
    }

    public void print(String str, int waitFlag, int nextFlag) {
        for (int i = 0; i < mLoopNumber; i++) {
            synchronized (this) {
                /**
                 * 情况1：t1 线程先执行到此处
                 * mFlag = 1，waitFlag = 1 条件不成立，直接输出 a，并将标志改为 2，然后唤醒线程 b c
                 * 这时候 t2 条件不满足 ，直接输出 b，t3 如果先获得锁，条件满足会一直等待
                 */
                while (mFlag != waitFlag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Sout.d(str);
                // 打印完后，将等待标记改为下个线程
                mFlag = nextFlag;
                // 唤醒其他线程
                this.notifyAll();
            }
        }
    }
}
