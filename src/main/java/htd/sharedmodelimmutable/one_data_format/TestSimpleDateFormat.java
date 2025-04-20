package htd.sharedmodelimmutable.one_data_format;

import htd.utils.Sout;

import java.text.SimpleDateFormat;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-19 15:52
 **/
public class TestSimpleDateFormat {
    /**
     * 由于 SimpleDateFormat 不是线程安全的
     * 有很大几率出现 java.lang.NumberFormatException 或者出现不正确的日期解析结果
     *
     * 2025-04-19 16:05:55.852	Thread-0	java.lang.NumberFormatException: multiple points
     * 2025-04-19 16:05:55.852	Thread-1	java.lang.NumberFormatException: multiple points
     * 2025-04-19 16:05:55.852	Thread-2	java.lang.NumberFormatException: empty String
     * 2025-04-19 16:05:55.852	Thread-5	java.lang.NumberFormatException: For input string: ""
     * 2025-04-19 16:05:55.852	Thread-4	java.lang.NumberFormatException: For input string: ""
     * 2025-04-19 16:05:55.854	Thread-9	Sat Apr 21 00:00:00 CST 1951
     * 2025-04-19 16:05:55.854	Thread-7	Sat Apr 21 00:00:00 CST 1951
     * 2025-04-19 16:05:55.854	Thread-6	Sat Apr 21 00:00:00 CST 1951
     * 2025-04-19 16:05:55.854	Thread-3	Wed Aug 21 00:00:00 CST 1202
     * 2025-04-19 16:05:55.854	Thread-8	Sat Apr 21 00:00:00 CST 1951
     */
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    Sout.d(sdf.parse("1951-04-21").toString());
                } catch (Exception e) {
                    Sout.d(e.toString());
                }
            }).start();
        }
    }
}
