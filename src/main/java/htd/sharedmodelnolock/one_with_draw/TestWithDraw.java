package htd.sharedmodelnolock.one_with_draw;

import htd.utils.Sout;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-05 22:23
 *
 * 有如下需求，保证 account.withdraw 取款方法的线程安全
 **/
public class TestWithDraw {
    public static void main(String[] args) {
        AccountUnsafe accountUnsafe = new AccountUnsafe(10000);
        Account.demo(accountUnsafe);
    }
}

interface Account {
    // 获取金额
    Integer getBalance();

    // 取款
    void withdraw(Integer amount);

    /**
     * 方法内会启动 1000 个线程，每个线程做 -10 元 的操作
     * 如果初始余额为 10000 那么正确的结果应当是 0
     */
    static void demo(Account account) {
        List<Thread> mList = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            mList.add(new Thread(() -> {
                account.withdraw(10);
            }));
        }
        // 遍历集合，开启每个线程
        mList.forEach(Thread::start);
        mList.forEach(t -> {
            try {
                // 等待每个线程都执行结束
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.nanoTime();
        Sout.d(account.getBalance() + " cost: " + (end - start) / 1000_000 + " ms");
    }
}

class AccountUnsafe implements Account {
    private Integer mBalance;

    public AccountUnsafe(Integer balance) {
        mBalance = balance;
    }

    @Override
    public Integer getBalance() {
        synchronized (this) {
            return mBalance;
        }
    }

    @Override
    public void withdraw(Integer amount) {
        synchronized (this) {
            mBalance -= amount;
        }
    }
}
