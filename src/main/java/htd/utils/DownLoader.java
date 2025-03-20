package htd.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: Concurrent
 * Create By: ChenFuXu
 * DateTime: 2022/9/18 22:22
 **/
public class DownLoader {
    public static List<String> download() {
        List<String> lines = new ArrayList<>();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://www.baidu.com").openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                    StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;

    }
}
