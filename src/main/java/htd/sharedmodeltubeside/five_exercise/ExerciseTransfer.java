package htd.sharedmodeltubeside.five_exercise;

import htd.utils.Sout;

import java.util.Random;

/**
 * Project: Concurrent
 * Create By: ChenFuXu
 * DateTime: 2022/9/13 23:05
 * <p>
 * 转账练习
 **/
public class ExerciseTransfer {
    // Random 线程安全
    private static Random sRandom = new Random();

    public static void main(String[] args) throws InterruptedException {
        Account accountA = new Account(1000);
        Account accountB = new Account(1000);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                accountA.transfer(accountB, randomAmount());
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                accountB.transfer(accountA, randomAmount());
            }
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        // 查看转账 2000 次后的总金额
        Sout.d("accountA.getMoney(): " + accountA.getMoney());
        Sout.d("accountB.getMoney(): " + accountB.getMoney());
    }

    // 随机 1-100
    private static int randomAmount() {
        return sRandom.nextInt(100) + 1;
    }
}

class Account {
    private int mMoney;

    public Account(int money) {
        this.mMoney = money;
    }

    public int getMoney() {
        return mMoney;
    }

    public void setMoney(int money) {
        this.mMoney = money;
    }

    public void transfer(Account target, int amount) {
        synchronized (Account.class) {
            if (this.mMoney > amount) {
                this.setMoney(this.getMoney() - amount);
                target.setMoney(target.getMoney() + amount);
            }
        }
    }
}
