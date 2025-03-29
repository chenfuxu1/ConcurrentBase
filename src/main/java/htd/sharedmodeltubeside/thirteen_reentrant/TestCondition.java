package htd.sharedmodeltubeside.thirteen_reentrant;

import htd.utils.Sout;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-29 8:02
 * <p>
 * Desc:
 * synchronized 中也有条件变量，就是我们讲原理时那个 waitSet 休息室，当条件不满足时进入 waitSet 等待
 * ReentrantLock 的条件变量比 synchronized 强大之处在于，它是支持多个条件变量的，这就好比
 * synchronized 是那些不满足条件的线程都在一间休息室等消息
 * 而 ReentrantLock 支持多间休息室，有专门等烟的休息室、专门等早餐的休息室、唤醒时也是按休息室来唤醒
 *
 * 使用要点：
 * await 前需要获得锁
 * await 执行后，会释放锁，进入 conditionObject 等待
 * await 的线程被唤醒（或打断、或超时）取重新竞争 lock 锁
 * 竞争 lock 锁成功后，从 await 后继续执行
 */
public class TestCondition {
    private static ReentrantLock sLock = new ReentrantLock();
    // 创建新的条件变量(休息室)
    private static Condition sWaitCigretteQueue = sLock.newCondition();
    private static Condition sWaitBreakfastQueue = sLock.newCondition();

    private static volatile boolean sHasCigarette = false;
    private static volatile boolean sHasBreakfast = false;

    /**
     * 2025-03-29 08:05:19.938	t1		开始等烟
     * 2025-03-29 08:05:19.938	t2		开始等早餐
     * 2025-03-29 08:05:20.933	main	送早餐来了
     * 2025-03-29 08:05:20.933	t2		等到了他的早餐
     * 2025-03-29 08:05:21.939	main	送烟来了
     * 2025-03-29 08:05:21.939	t1		等到了他的烟
     */
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                sLock.lock();
                while (!sHasCigarette) {
                    try {
                        // 在休息室等待，不在 room 等待了
                        Sout.d("开始等烟");
                        sWaitCigretteQueue.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Sout.d("等到了他的烟");
            } finally {
                sLock.unlock();
            }
        }, "t1").start();

        new Thread(() -> {
            try {
                sLock.lock();
                while (!sHasBreakfast) {
                    try {
                        // 在休息室等待，不在 room 等待了
                        Sout.d("开始等早餐");
                        sWaitBreakfastQueue.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Sout.d("等到了他的早餐");
            } finally {
                sLock.unlock();
            }
        }, "t2").start();

        try {
            Thread.sleep(1000);
            sendBreakfast();
            Thread.sleep(1000);
            sendCigarette();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void sendCigarette() {
        sLock.lock();
        try {
            Sout.d("送烟来了");
            sHasCigarette = true;
            sWaitCigretteQueue.signal(); // 唤醒
        } finally {
            sLock.unlock();
        }
    }

    private static void sendBreakfast() {
        sLock.lock();
        try {
            Sout.d("送早餐来了");
            sHasBreakfast = true;
            sWaitBreakfastQueue.signal(); // 唤醒
        } finally {
            sLock.unlock();
        }
    }
}
