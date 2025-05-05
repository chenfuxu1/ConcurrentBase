package htd.sharedmodeltool.thread_pool.thread_pool_executor;

import htd.utils.Sout;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-05-03 21:37
 * <p>
 * 如何让每周四 18:00:00 定时执行任务？
 **/
public class TestOrderTime {
    private static final String TAG = "TestOrderTime";

    /**
     * 025-05-03 21:43:19.661	main		TestOrderTime	2025-05-03T21:43:19.648
     * 2025-05-03 21:43:19.662	main		TestOrderTime	2025-05-08T18:00
     * 2025-05-03 21:43:19.662	main		TestOrderTime	418600352
     */
    public static void main(String[] args) {
        // 1.获取当前时间
        LocalDateTime now = LocalDateTime.now(); // 线程安全
        Sout.d(TAG, now.toString());
        // 2.获取周四的时间
        LocalDateTime time = now.withHour(18).withMinute(0).withSecond(0).withNano(0).with(DayOfWeek.THURSDAY);
        // 如果当前时间大于本周周四，必须要找到下周的周四
        if (now.compareTo(time) > 0) {
            time = time.plusWeeks(1);
        }
        Sout.d(TAG, time.toString());
        // 3.获取周四的时间与当前时间的间隔时间
        long initialDelay = Duration.between(now, time).toMillis();
        Sout.d(TAG, String.valueOf(initialDelay));
        // 4.一周的间隔时间
        long period = 1000 * 60 * 60 * 24 * 7;
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        pool.scheduleAtFixedRate(() -> {
            Sout.d("执行任务");
        }, initialDelay, period, TimeUnit.MILLISECONDS);
    }
}
