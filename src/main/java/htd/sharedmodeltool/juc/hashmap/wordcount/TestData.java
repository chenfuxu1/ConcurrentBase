package htd.sharedmodeltool.juc.hashmap.wordcount;

import htd.utils.Sout;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-06-07 8:13
 * <p>
 * Desc: 生成测试数据
 */
public class TestData {
    static final String ALPHA = "abcedfghijklmnopqrstuvwxyz";

    public static void main(String[] args) {
        int length = ALPHA.length();
        int count = 200;
        List<String> list = new ArrayList<>(length * count);
        for (int i = 0; i < length; i++) {
            char ch = ALPHA.charAt(i);
            for (int j = 0; j < count; j++) {
                list.add(String.valueOf(ch));
            }
        }
        System.out.println("aaa");
        Collections.shuffle(list);
        for (int i = 0; i < 26; i++) {
            System.out.println("aaaaa");
            try (PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(
                            new FileOutputStream("src/test/" + (i + 1) + ".txt")))) {
                String collect = list.subList(i * count, (i + 1) * count).stream()
                        .collect(Collectors.joining("\n"));
                out.print(collect);
            } catch (IOException e) {
            }
        }
    }
}
