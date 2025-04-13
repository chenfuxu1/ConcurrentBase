package htd.sharedmodelnolock.one_with_draw;

import htd.utils.Sout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-05 22:26
 *
 * 有如下需求，保证 account.withdraw 取款方法的线程安全
 * 无锁实现
 **/
public class TestWithDraw2 {
    public static void main(String[] args) {
        // 2025-04-05 22:27:33.204	main    9990 cost: 31 ms
        AccountUnsafe2 accountUnsafe = new AccountUnsafe2(10000);
        Account2.demo(accountUnsafe);
    }
}

interface Account2 {
    // 获取金额
    Integer getBalance();

    // 取款
    void withdraw(Integer amount);

    /**
     * 方法内会启动 1000 个线程，每个线程做 -10 元 的操作
     * 如果初始余额为 10000 那么正确的结果应当是 0
     */
    static void demo(Account2 account) {
        List<Thread> mList = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < 1; i++) {
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

class AccountUnsafe2 implements Account2 {
    private AtomicInteger mBalance;

    public AccountUnsafe2(Integer balance) {
        this.mBalance = new AtomicInteger(balance);
    }

    @Override
    public Integer getBalance() {
        return mBalance.get();
    }

    @Override
    public void withdraw(Integer amount) {
        while (true) {
            // 获取当前值
            int prev = mBalance.get();
            // 计算要改写的值
            int next = prev - amount;
            /**
             * 比较并设置
             * mBalance.compareAndSet(prev, next) 返回布尔值
             * 设置成功，返回 true，结束循环
             * 设置失败，返回 false，下次继续设置
             */
            if (mBalance.compareAndSet(prev, next)) {
                break;
            }

            // 可以简化为下面的方法
            // mBalance.addAndGet(-1 * amount);
        }
    }
}
