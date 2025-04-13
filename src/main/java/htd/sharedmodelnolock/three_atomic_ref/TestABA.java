package htd.sharedmodelnolock.three_atomic_ref;

import htd.utils.Sout;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-05 23:07
 *
 * ABA 问题
 * 一个属性被其他线程改变之后，变为 A -> B
 * 又被修改回来，B -> A
 **/
public class TestABA {
    private static AtomicReference<String> sAtomicReference = new AtomicReference<>("A");

    /**
     * 2025-04-05 23:07:54.795	main	开始运行
     * 2025-04-05 23:07:54.823	t1		改变 A -> B : true
     * 2025-04-05 23:07:55.336	t2		改变 B -> A : true
     * 2025-04-05 23:07:56.344	main	尝试改为 C：true
     */
    public static void main(String[] args) {
        Sout.d("开始运行");
        /**
         * 只能在该线程中获取到当前共享变量的值
         * 但后续该值有没有被其他线程修改过却并不知道
         */
        String prev = sAtomicReference.get();
        other();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         * 这时在主线程中并不知到该值已经被其他线程修改过
         * 尝试改为 C，修改成功了
         */
        Sout.d("尝试改为 C：" + sAtomicReference.compareAndSet(prev, "C"));
    }

    /**
     * 将 A 值修改为 B
     * 又将 B 值修改为 A
     */
    private static void other() {
        new Thread(() -> {
            Sout.d("改变 A -> B : " + sAtomicReference.compareAndSet(sAtomicReference.get(), "B"));
        }, "t1").start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            Sout.d("改变 B -> A : " + sAtomicReference.compareAndSet(sAtomicReference.get(), "A"));
        }, "t2").start();

    }
}
