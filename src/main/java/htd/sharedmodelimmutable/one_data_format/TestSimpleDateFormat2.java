package htd.sharedmodelimmutable.one_data_format;

import htd.utils.Sout;

import java.text.SimpleDateFormat;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-19 15:52
 **/
public class TestSimpleDateFormat2 {
    /**
     * 2025-04-19 16:09:10.077	Thread-0	Sat Apr 21 00:00:00 CST 1951
     * 2025-04-19 16:09:10.077	Thread-9	Sat Apr 21 00:00:00 CST 1951
     * 2025-04-19 16:09:10.077	Thread-8	Sat Apr 21 00:00:00 CST 1951
     * 2025-04-19 16:09:10.077	Thread-7	Sat Apr 21 00:00:00 CST 1951
     * 2025-04-19 16:09:10.077	Thread-6	Sat Apr 21 00:00:00 CST 1951
     * 2025-04-19 16:09:10.077	Thread-5	Sat Apr 21 00:00:00 CST 1951
     * 2025-04-19 16:09:10.077	Thread-4	Sat Apr 21 00:00:00 CST 1951
     * 2025-04-19 16:09:10.077	Thread-3	Sat Apr 21 00:00:00 CST 1951
     * 2025-04-19 16:09:10.077	Thread-2	Sat Apr 21 00:00:00 CST 1951
     * 2025-04-19 16:09:10.077	Thread-1	Sat Apr 21 00:00:00 CST 1951
     */
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                synchronized (sdf) {
                    try {
                        Sout.d(sdf.parse("1951-04-21").toString());
                    } catch (Exception e) {
                        Sout.d(e.toString());
                    }
                }

            }).start();
        }
    }
}
