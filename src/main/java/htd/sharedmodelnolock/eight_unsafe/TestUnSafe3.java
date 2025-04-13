package htd.sharedmodelnolock.eight_unsafe;

import htd.utils.Sout;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-13 17:25
 * <p>
 * Desc:
 */
public class TestUnSafe3 {
    public static void main(String[] args) {
        Account.demo(new MyAtomicInteger(10000));
    }
}

/**
 * 自定义原子整数类
 */
class MyAtomicInteger implements Account {

    private volatile int mValue;
    private static final long sValueOffset; // 内存偏移量
    private static final Unsafe UNSAFE;

    static {
        UNSAFE = UnsafeAccessor.getUnsafe();
        try {
            sValueOffset = UNSAFE.objectFieldOffset(MyAtomicInteger.class.getDeclaredField("mValue"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public MyAtomicInteger(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }

    public void decrement(int amount) {
        while (true) {
            int prev = this.mValue;
            int next = prev - amount;
            // 设置成功，结束循环
            if (UNSAFE.compareAndSwapInt(this, sValueOffset, prev, next)) {
                break;
            }
        }
    }

    @Override
    public Integer getBalance() {
        return mValue;
    }

    @Override
    public void withdraw(Integer amount) {
        decrement(amount);
    }
}

class UnsafeAccessor {
    private static Unsafe sUnsafe;

    public static Unsafe getUnsafe() {
        try {
            Field unSafe = Unsafe.class.getDeclaredField("theUnsafe");
            unSafe.setAccessible(true);
            sUnsafe = (Unsafe) unSafe.get(null);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return sUnsafe;
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