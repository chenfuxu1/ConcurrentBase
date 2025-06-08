package htd.sharedmodeltool.juc.hashmap.deadchain;

import java.util.HashMap;


/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-06-07 8:58
 * <p>
 * Desc: JDK 7 HashMap 并发死链
 * <p>
 * 要在 JDK 7 下运行，否则扩容机制和 hash 的计算方法都变了
 */
public class TestDeadChain {
    private static final String TAG = "TestDeadChain";

    public static void main(String[] args) {
        // 测试 java 7 中哪些数字的 hash 结果相等
        System.out.println("长度为 16 时，桶下标为 1 的 key");
        for (int i = 0; i < 64; i++) {
            if (hash(i) % 16 == 1) {
                System.out.println(i);
            }
        }
        System.out.println("长度为 32 时，桶下标为 1 的 key");
        for (int i = 0; i < 64; i++) {
            if (hash(i) % 32 == 1) {
                System.out.println(i);
            }
        }
        // 1, 35, 16, 50 当大小为 16 时，它们在一个桶内
        final HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        // 放 12 个元素
        map.put(2, null);
        map.put(3, null);
        map.put(4, null);
        map.put(5, null);
        map.put(6, null);
        map.put(7, null);
        map.put(8, null);
        map.put(9, null);
        map.put(10, null);
        map.put(16, null);
        map.put(35, null);
        map.put(1, null);
        System.out.println("扩容前大小[main]: " + map.size());

        new Thread() {
            @Override
            public void run() {
                // 放第 13 个元素, 发生扩容
                map.put(50, null);
                System.out.println("扩容后大小[Thread-0]: " + map.size());
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                // 放第 13 个元素, 发生扩容
                map.put(50, null);
                System.out.println("扩容后大小[Thread-1]: " + map.size());
            }
        }.start();
    }

    final static int hash(Object k) {
        int h = 0;
        if (0 != h && k instanceof String) {
            // return sun.misc.Hashing.stringHash32((String) k); jdk7 放开注释
        }
        h ^= k.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
}
