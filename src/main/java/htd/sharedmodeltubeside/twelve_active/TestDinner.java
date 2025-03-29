package htd.sharedmodeltubeside.twelve_active;

import htd.utils.Sout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-24 23:36
 * <p>
 * Desc: 哲学家就餐问题
 * 有五位哲学家，围坐在圆桌旁。
 * 他们只做两件事，思考和吃饭，思考一会吃口饭，吃完饭后接着思考。
 * 吃饭时要用两根筷子吃，桌上共有 5 根筷子，每位哲学家左右手边各有一根筷子。
 * 如果筷子被身边的人拿着，自己就得等待
 */
public class TestDinner {
    public static void main(String[] args) {
        ChopStick c1 = new ChopStick("1");
        ChopStick c2 = new ChopStick("2");
        ChopStick c3 = new ChopStick("3");
        ChopStick c4 = new ChopStick("4");
        ChopStick c5 = new ChopStick("5");
        new Philosopher("苏格拉底", c1, c2).start();
        new Philosopher("柏拉图", c2, c3).start();
        new Philosopher("亚里士多德", c3, c4).start();
        new Philosopher("赫拉克里特", c4, c5).start();
        new Philosopher("阿基米德", c5, c1).start();
    }
}

/**
 * 筷子类
 */
class ChopStick {
    private String mName;

    public ChopStick(String name) {
        mName = name;
    }

    @Override
    public String toString() {
        return "ChopStick{" +
                "mName='" + mName + '\'' +
                '}';
    }
}

/**
 * 哲学家类
 */
class Philosopher extends Thread {
    private ChopStick mLeft;
    private ChopStick mRight;

    public Philosopher(String name, ChopStick left, ChopStick right) {
        super(name);
        this.mLeft = left;
        this.mRight = right;
    }

    private void eat() {
        Sout.d("eating ...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            // 获得左手筷子
            synchronized (mLeft) {
                // 获得右手筷子
                synchronized (mRight) {
                    // 开始吃饭
                    eat();
                }
                // 放下右手的筷子
            }
            // 放下左手的筷子
        }
    }
}
