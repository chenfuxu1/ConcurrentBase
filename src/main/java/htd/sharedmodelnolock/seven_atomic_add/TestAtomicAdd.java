package htd.sharedmodelnolock.seven_atomic_add;

import htd.utils.Sout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-13 8:31
 * <p>
 * Desc:
 * 原子累加器
 * 累加器性能比较
 */
public class TestAtomicAdd {
    /**
     * 2025-04-13 08:34:34.230	main    20000000 cost time: 60 ms
     * 2025-04-13 08:34:34.241	main    20000000 cost time: 11 ms
     * 2025-04-13 08:34:34.249	main    20000000 cost time: 8 ms
     * 2025-04-13 08:34:34.267	main    20000000 cost time: 18 ms
     * 2025-04-13 08:34:34.277	main    20000000 cost time: 10 ms
     * ===================
     * 2025-04-13 08:34:34.567	main    20000000 cost time: 290 ms
     * 2025-04-13 08:34:34.877	main    20000000 cost time: 309 ms
     * 2025-04-13 08:34:35.155	main    20000000 cost time: 278 ms
     * 2025-04-13 08:34:35.483	main    20000000 cost time: 328 ms
     * 2025-04-13 08:34:35.707	main    20000000 cost time: 224 ms
     */
    public static void main(String[] args) {
        /**
         * 性能提升的原因很简单，就是在有竞争时，设置多个累加单元，Therad-0 累加 Cell[0]，而 Thread-1 累加
         * Cell[1]... 最后将结果汇总。这样它们在累加时操作的不同的 Cell 变量，因此减少了 CAS 重试失败，从而提高性
         * 能。
         */
        for (int i = 0; i < 5; i++) {
            demo(() -> new LongAdder(), adder -> adder.increment());
        }

        System.out.println("===================");

        for (int i = 0; i < 5; i++) {
            demo(() -> new AtomicLong(), adder -> adder.getAndIncrement());
        }
    }

    /**
     * @param adderSupplier 提供者 () -> 结果
     * @param action        消费者 (参数) -> 结果
     * @param <T>
     */
    private static <T> void demo(Supplier<T> adderSupplier, Consumer<T> action) {
        T adder = adderSupplier.get();
        long start = System.currentTimeMillis();
        List<Thread> list = new ArrayList<>();
        /**
         * 40 个线程，每人累加 50 万
         */
        for (int i = 0; i < 40; i++) {
            list.add(new Thread(() -> {
                for (int j = 0; j < 500000; j++) {
                    action.accept(adder);
                }
            }));
        }
        // 启动所有线程
        list.forEach(t -> t.start());
        // 等待所有线程结束
        list.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.currentTimeMillis();
        Sout.d(adder + " cost time: " + (end - start) + " ms");
    }
}
