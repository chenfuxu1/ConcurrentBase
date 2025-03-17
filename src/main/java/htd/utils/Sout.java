package htd.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-09 16:37
 * <p>
 * Desc:
 */
public class Sout {
    private static SimpleDateFormat sSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static void d(String msg) {
        String format = sSimpleDateFormat.format(Calendar.getInstance().getTime());
        System.out.println(format + "\t" + Thread.currentThread().getName() + "\t\t" + msg);
    }

    public static void d(String tag, String msg) {
        String format = sSimpleDateFormat.format(Calendar.getInstance().getTime());
        System.out.println(format + "\t" + Thread.currentThread().getName() + "\t\t" + tag + "\t"  + msg);
    }

    public static void d(String tag, String msg, Throwable throwable) {
        String format = sSimpleDateFormat.format(Calendar.getInstance().getTime());
        System.out.println(format + "\t" + Thread.currentThread().getName() + "\t\t" + tag + "\t"  + msg + "\n" + throwable.toString());
    }
}
