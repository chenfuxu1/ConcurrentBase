package htd.sharedmodeltubeside.eleven_multi_lock;

import htd.utils.Sout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-24 23:19
 * <p>
 * Desc:
 */
public class TestMultiLock {
    public static void main(String[] args) {
        test1();

        // test2();
    }

    private static void test1() {
        // 并发度很低，一个线程必须等待另外一个线程执行完毕，释放锁才能执行
        BigRoom bigRoom = new BigRoom();
        new Thread(bigRoom::study, "小南").start();

        new Thread(bigRoom::sleep, "小女").start();
    }

    private static void test2() {
        System.out.println("=====================");
        // 并发度很低，一个线程必须等待另外一个线程执行完毕，释放锁才能执行
        BigRoomImprove bigRoom = new BigRoomImprove();
        new Thread(bigRoom::study, "小南").start();

        new Thread(bigRoom::sleep, "小女").start();
    }

}

class BigRoom {
    // 睡觉
    public void sleep() {
        synchronized (this) {
            Sout.d("sleeping 2 小时");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 学习
    public void study() {
        synchronized (this) {
            Sout.d("study 1 小时");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * 改进，准备多个房间（多个对象锁）
 * 将锁的粒度细分
 * 好处，是可以增强并发度
 * 坏处，如果一个线程需要同时获得多把锁，就容易发生死锁
 */
class BigRoomImprove {
    private final Object studyRoom = new Object();
    private final Object bedRoom = new Object();

    // 睡觉
    public void sleep() {
        synchronized (bedRoom) {
            Sout.d("sleeping 2 小时");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 学习
    public void study() {
        synchronized (studyRoom) {
            Sout.d("study 1 小时");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
