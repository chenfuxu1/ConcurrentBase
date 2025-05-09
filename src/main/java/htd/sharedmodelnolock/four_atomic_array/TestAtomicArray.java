package htd.sharedmodelnolock.four_atomic_array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-13 7:56
 * <p>
 * Desc:
 * AtomicIntegerArray
 * AtomicLongArray
 * AtomicReferenceArray
 */
public class TestAtomicArray {
    public static void main(String[] args) {
        // 不安全的数组
        demo(
                () -> new int[10],
                (array) -> array.length,
                (array, index) -> array[index]++,
                array -> System.out.println(Arrays.toString(array))
        );

        System.out.println("==============");

        // 安全数组
        demo(
                () -> new AtomicIntegerArray(10),
                (array) -> array.length(),
                (array, index) -> array.getAndIncrement(index),
                array -> System.out.println(array)
        );
    }

    /**
     * 参数1，提供数组、可以是线程不安全数组或线程安全数组
     * 参数2，获取数组长度的方法
     * 参数3，自增方法，回传 array, index
     * 参数4，打印数组的方法
     * <p>
     * supplier 提供者 无中生有        () -> 结果
     * function 函数 一个参数一个结果  (参数) -> 结果，BiFunction (参数 1, 参数 2) -> 结果
     * consumer 消费者 一个参数没结果  (参数) -> void，BiConsumer (参数 1，参数 2) -> 结果
     */
    private static <T> void demo(
            Supplier<T> arraySupplier,
            Function<T, Integer> lengthFun,
            BiConsumer<T, Integer> putConsumer,
            Consumer<T> printConsumer
    ) {
        List<Thread> list = new ArrayList<>();
        T array = arraySupplier.get();
        int length = lengthFun.apply(array);
        for (int i = 0; i < length; i++) {
            list.add(new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    putConsumer.accept(array, j % length);

                }
            }));
        }

        // 启动所有线程
        list.forEach(t -> t.start());
        list.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }); // 等待所有线程结束
        printConsumer.accept(array);
    }
}
