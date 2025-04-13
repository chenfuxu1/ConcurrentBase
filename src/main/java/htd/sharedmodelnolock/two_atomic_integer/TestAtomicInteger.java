package htd.sharedmodelnolock.two_atomic_integer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-05 22:53
 **/
public class TestAtomicInteger {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        /**
         * 先增加再获取值
         * 相当于 ++i
         * 输出 1
         * 都是原子操作
         */
        System.out.println(atomicInteger.incrementAndGet()); // 1

        // 先获取，再自增，相当于 i++，输出 1，值变为 2
        System.out.println(atomicInteger.getAndIncrement()); // 1

        /**
         * 先获取再增加 5
         * 输出 2，值变为 7
         */
        System.out.println(atomicInteger.getAndAdd(5)); // 2

        /**
         * 先增加 5 再获取
         * 输出 12，值为 12
         */
        System.out.println(atomicInteger.addAndGet(5)); // 12

        /**
         * 更新并获取，先更新值，再获取值
         * value -> value * 10
         * value: 当前值，为 12
         * value * 10：要设置的值，为 120
         * 所以输出 120，值为 120
         * 其中函数中的操作能保证原子，但函数需要无副作用
         */
        System.out.println(atomicInteger.updateAndGet(value -> value * 10)); // 120

        /**
         * 获取并更新，先获取值，再更新
         * value -> value * 2
         * value: 当前值，为 120
         * value * 2：要设置的值，为 240
         * 所以输出 120，值为 240
         * 其中函数中的操作能保证原子，但函数需要无副作用
         */
        System.out.println(atomicInteger.getAndUpdate(value -> value * 2)); // 120

        /**
         * 获取并计算（p 为 i 的当前值 240, x 为参数1, 10，执行 240 + 10 结果 i = 250, 返回 240）
         * 其中函数中的操作能保证原子，但函数需要无副作用
         * getAndUpdate 如果在 lambda 中引用了外部的局部变量，要保证该局部变量是 final 的
         * getAndAccumulate 可以通过 参数1 来引用外部的局部变量，但因为其不在 lambda 中因此不必是 final
         */
        System.out.println(atomicInteger.getAndAccumulate(10, (p, x) -> p + x)); // 240

        /**
         * 计算并获取（p 为 i 的当前值 250, x 为参数1, 执行 250 - 10，结果 i = 240, 返回 240）
         * 其中函数中的操作能保证原子，但函数需要无副作用
         */
        System.out.println(atomicInteger.accumulateAndGet(-10, (p, x) -> p + x)); // 240

        testUpdateAndGet();
    }

    // 自定义 updateAndGet 方法
    private static void testUpdateAndGet() {
        AtomicInteger atomicInteger = new AtomicInteger(20);
        updateAndGet(atomicInteger, value -> value / 2);
        System.out.println(atomicInteger.get()); // 10

    }

    private static void updateAndGet(AtomicInteger atomicInteger, IntUnaryOperator operator) {
        while (true) {
            // 获取当前值
            int prev = atomicInteger.get();
            /**
             * 根据当前值，计算要设置的值
             * 这里将要计算的操作交给 IntUnaryOperator 接口
             */
            int next = operator.applyAsInt(prev);
            if (atomicInteger.compareAndSet(prev, next)) {
                // 设置成功，结束循环
                break;
            }
        }
    }
}
