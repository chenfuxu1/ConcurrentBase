package htd.sharedmodeltubeside.five_exercise;

import htd.utils.Sout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * Project: Concurrent
 * Create By: ChenFuXu
 * DateTime: 2022/9/10 11:55
 *
 * 卖票练习
 **/
public class ExerciseSell {
    // Random 线程安全
    private static Random sRandom = new Random();

    public static void main(String[] args) {
        TicketWindow ticketWindow = new TicketWindow(2000);
        List<Thread> list = new ArrayList<>();
        // 用来存储卖出去多少张票
        List<Integer> sellCount = new Vector<>();
        for (int i = 0; i < 2000; i++) {
            Thread thread = new Thread(() -> {
                // 分析这里的竟态条件
                int count = ticketWindow.sell(randomAmount());
                sellCount.add(count);
            });
            list.add(thread);
            thread.start();
        }
        list.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 卖出去的票求和
        Sout.d("卖出去的总和：" + sellCount.stream().mapToInt(c -> c).sum());
        // 剩余票数
        Sout.d("剩余票数：" + ticketWindow.getCount());
    }

    // 随机数 1-5
    private static int randomAmount() {
        return sRandom.nextInt(5) + 1;
    }
}

class TicketWindow {
    private int mCount;

    public TicketWindow(int count) {
        this.mCount = count;
    }

    public int getCount() {
        return mCount;
    }

    public synchronized int sell(int amount) {
        if (this.mCount >= amount) {
            this.mCount -= amount;
            return amount;
        } else {
            return 0;
        }
    }
}
