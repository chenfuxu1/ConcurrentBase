package htd.sharedmodelnolock.three_atomic_ref;

import htd.utils.Sout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-05 22:59
 *
 * 有如下需求，保证 account.withdraw 取款方法的线程安全
 **/
public class TestDecimalAccount {
    public static void main(String[] args) {
        DecimalAccount.demo(new DecimalAccountCas(new BigDecimal("10000")));
    }
}

interface DecimalAccount {
    // 获取金额
    BigDecimal getBalance();

    // 取款
    void withdraw(BigDecimal amount);

    /**
     * 方法内会启动 1000 个线程，每个线程做 -10 元 的操作
     * 如果初始余额为 10000 那么正确的结果应当是 0
     */
    static void demo(DecimalAccount account) {
        List<Thread> mList = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            mList.add(new Thread(() -> {
                account.withdraw(BigDecimal.TEN);
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

class DecimalAccountCas implements DecimalAccount {
    private AtomicReference<BigDecimal> mBalance;

    public DecimalAccountCas(BigDecimal balance) {
        this.mBalance = new AtomicReference<>(balance);
    }

    @Override
    public BigDecimal getBalance() {
        return mBalance.get();
    }

    @Override
    public void withdraw(BigDecimal amount) {
        while (true) {
            BigDecimal prev = mBalance.get();
            BigDecimal next = prev.subtract(amount);
            if (mBalance.compareAndSet(prev, next)) {
                break;
            }
        }
    }
}
