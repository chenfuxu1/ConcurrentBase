package htd.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Project: Concurrent
 * Create By: ChenFuXu
 * DateTime: 2022/9/2 23:24
 * <p>
 * 读取文件
 **/
public class FileReader {
    public static void read(String fileName) {
        int index = fileName.lastIndexOf(File.separator);
        String shortName = fileName.substring(index + 1);
        try (FileInputStream in = new FileInputStream(fileName)) {
            long start = System.currentTimeMillis();
            Sout.d("开始读取文件：" + shortName);
            byte[] buf = new byte[1024];
            int n = -1;
            do {
                n = in.read(buf);
            } while (n != -1);
            long end = System.currentTimeMillis();
            Sout.d("读取文件结束：" + shortName);
            Sout.d("消耗时间：" + (end - start) + "ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
