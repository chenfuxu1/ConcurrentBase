package htd.sharedmodeltool.juc.hashmap.wordcount;

import htd.utils.Sout;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-06-07 7:59
 * <p>
 * Desc:
 */
public class TestWordCount {
    private static final String TAG = "TestWordCount";

    public static void main(String[] args) {
        // test1();
        // test2();
        // test3();
        test4();
    }

    private static void test1() {
        demo(
                // 创建 map 集合
                // 创建 ConcurrentHashMap 对不对？
                () -> new HashMap<String, Integer>(),
                // 进行计数
                (map, words) -> {
                    for (String word : words) {
                        Integer counter = map.get(word);
                        int newValue = counter == null ? 1 : counter + 1;
                        map.put(word, newValue);
                    }
                }
        );
    }

    private static void test2() {
        demo(
                // 创建 map 集合
                // 创建 ConcurrentHashMap 对不对？
                () -> new ConcurrentHashMap<String, Integer>(),
                // 进行计数
                (map, words) -> {
                    for (String word : words) {
                        Integer counter = map.get(word);
                        int newValue = counter == null ? 1 : counter + 1;
                        map.put(word, newValue);
                    }
                }
        );
    }

    private static void test3() {
        demo(
                () -> new ConcurrentHashMap<String, LongAdder>(),
                // 进行计数
                (map, words) -> {
                    for (String word : words) {
                        // // 1.检查 key，有没有，没有则生成一个 value
                        // Integer counter = map.get(word);
                        // int newValue = counter == null ? 1 : counter + 1;
                        // // 2.没有则 put，有，有就用上次的值累加
                        // map.put(word, newValue);

                        // 注意不能使用 putIfAbsent，此方法返回的是上一次的 value，首次调用返回 null
                        map.computeIfAbsent(word, (key) -> new LongAdder()).increment();
                    }
                }
        );
    }

    private static void test4() {
        demo(
                () -> new ConcurrentHashMap<String, Integer>(),
                // 进行计数
                (map, words) -> {
                    for (String word : words) {
                        // // 1.检查 key，有没有，没有则生成一个 value
                        // Integer counter = map.get(word);
                        // int newValue = counter == null ? 1 : counter + 1;
                        // // 2.没有则 put，有，有就用上次的值累加
                        // map.put(word, newValue);


                        map.merge(word, 1, Integer::sum);
                    }
                }
        );
    }

    /**
     * supplier 提供者 无中生有        () -> 结果
     * consumer 消费者 一个参数没结果  (参数) -> void，BiConsumer (参数 1，参数 2) -> 结果
     */
    private static <T> void demo(
            Supplier<Map<String, T>> supplier,
            BiConsumer<Map<String, T>, List<String>> consumer
    ) {
        Map<String, T> counterMap = supplier.get();
        List<Thread> ts = new ArrayList<>();
        for (int i = 1; i <= 26; i++) {
            int idx = i;
            Thread thread = new Thread(() -> {
                List<String> words = readFromFile(idx);
                consumer.accept(counterMap, words);
            });
            ts.add(thread);
        }
        ts.forEach(t -> t.start());
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Sout.d(TAG, "counterMap: " + counterMap);
    }

    public static List<String> readFromFile(int i) {
        ArrayList<String> words = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("src/test/"
                + i + ".txt")))) {
            while (true) {
                String word = in.readLine();
                if (word == null) {
                    break;
                }
                words.add(word);
            }
            return words;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
