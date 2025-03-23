package htd.sharedmodeltubeside.two_sync;


import htd.utils.Sout;
/**
 * Project: Concurrent
 * Create By: ChenFuXu
 * DateTime: 2022/9/4 22:42
 * <p>
 * 通过面向对象的方式对 synchronized 进行改进
 * 把需要保护的共享变量放入一个类
 **/
public class TestSynchronized {
    public static void main(String[] args) {
        Room room = new Room();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.increment();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.decrement();
            }
        }, "t2");

        try {
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            Sout.d("room.getValue(): " + room.getValue());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Room {
    private int mValue = 0;

    public void increment() {
        /**
         * 对 对象自己进行加锁
         */
        synchronized (this) {
            mValue++;
        }
    }

    public void decrement() {
        /**
         * 对 对象自己进行加锁
         */
        synchronized (this) {
            mValue--;
        }
    }

    /**
     * 为了保证获取的时候得到准确的结果，而不是中间的结果
     * 这里也要加锁
     *
     * @return
     */
    public int getValue() {
        synchronized (this) {
            return mValue;
        }
    }
}
