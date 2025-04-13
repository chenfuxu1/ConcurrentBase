package htd.sharedmodelnolock.three_atomic_ref;

import htd.utils.Sout;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-05 23:10
 *
 * ABA 问题
 * 一个属性被其他线程改变之后，变为 A -> B
 * 又被修改回来，B -> A
 * 使用 AtomicStampedReference
 **/
public class TestAtomicStampedReference {
    private static AtomicStampedReference<String> sAtomicStampedReference = new AtomicStampedReference<>("A", 0);

    /**
     * 2025-04-05 23:11:48.368	main	开始运行
     * 2025-04-05 23:11:48.368	main	主线程当前版本：0
     * 2025-04-05 23:11:48.401	t1		t1 线程版本号：0
     * 2025-04-05 23:11:48.401	t1		改变 A -> B : true
     * 2025-04-05 23:11:48.903	t2		t2 线程版本号：1
     * 2025-04-05 23:11:48.903	t2		改变 B -> A : true
     * 2025-04-05 23:11:49.918	main	主线程目前的版本号：0
     * 2025-04-05 23:11:49.918	main	尝试改为 C：false
     */
    public static void main(String[] args) {
        Sout.d("开始运行");
        String prev = sAtomicStampedReference.getReference();
        // 获取版本号
        int stamp = sAtomicStampedReference.getStamp(); // 版本号为 0
        Sout.d("主线程当前版本：" + stamp);
        // 如果中间有其他线程干扰，发生了 ABA 现象
        other(); // 已经将版本号修改为 2 了，因为修改程了两次
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         * 这里不仅比较获取到 prev 值和主存的值是否相等
         * 还比较获取的版本号 stamp 和 主存的 stamp 是否一致，一致表名没有修改过
         * 这里 stamp 为 0，实际主存的 stamp 已经是 2 了
         * 尝试改为 C，stamp 版本号不一致，所以修改失败
         */
        Sout.d("主线程目前的版本号：" + stamp);
        Sout.d("尝试改为 C：" + sAtomicStampedReference.compareAndSet(prev, "C", stamp, stamp + 1));
    }

    /**
     * 将 A 值修改为 B
     * 又将 B 值修改为 A
     * 修改成功后会将版本号加 1
     */
    private static void other() {
        new Thread(() -> {
            int stamp = sAtomicStampedReference.getStamp();
            Sout.d("t1 线程版本号：" + stamp);
            Sout.d("改变 A -> B : " + sAtomicStampedReference.compareAndSet(sAtomicStampedReference.getReference(),
                    "B", stamp, stamp + 1)); // 版本号为 1
        }, "t1").start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            int stamp = sAtomicStampedReference.getStamp(); // 版本号为 1
            Sout.d("t2 线程版本号：" + stamp);
            Sout.d("改变 B -> A : " + sAtomicStampedReference.compareAndSet(sAtomicStampedReference.getReference(),
                    "A", stamp, stamp + 1)); // 版本号为 2
        }, "t2").start();
    }
}
