package htd.javathread.six_boiled_water;

import htd.utils.Sout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-20 23:00
 * <p>
 * Desc: 案例：应用之统筹
 * 烧水泡茶
 * t1：洗水壶 1 分钟，烧开水 15 分钟
 * t2：洗茶壶、洗茶杯、拿茶叶，共 4 分钟
 * 然后泡茶
 * <p>
 * 缺陷：
 * 1、只模拟了 t2 等待 t1 的情况，没模拟 t2 准备好了等待 t1 的情况
 * 2、没能实现线程间的通信，例如 t1 水烧开了，怎么把水壶交给 t2
 */
public class TestTea {
    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        Thread t1 = new Thread(() -> {
            try {
                Sout.d("开始洗水壶");
                Thread.sleep(1000);
                Sout.d("开始烧开水");
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            try {
                Sout.d("开始洗茶壶");
                Thread.sleep(1000);
                Sout.d("开始洗杯子");
                Thread.sleep(2000);
                Sout.d("开始拿茶叶");
                Thread.sleep(1000);
                Sout.d("等待开始泡茶");
                /**
                 * 等待 t1 线程执行完毕，表明水烧开了
                 * 才开始泡茶
                 */
                t1.join();
                Sout.d("开始泡茶啦...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2");

        t1.start();
        t2.start();
    }
}
