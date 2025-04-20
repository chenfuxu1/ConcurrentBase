package htd.sharedmodelimmutable.one_data_format;

import htd.utils.Sout;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-19 16:10
 *
 * 前面日期转换的问题可以使用加锁来解决，但是会损耗性能
 * 如果一个对象在不能够修改其内部状态（属性），那么它就是线程安全的，因为不存在并发修改啊！这样的对象在
 * Java 中有很多，例如在 Java 8 后，提供了一个新的日期格式化类：
 **/
public class TestDateTimeFormatter {
    public static void main(String[] args) {
        /**
         * 2025-04-19 16:11:41.885	Thread-1	{},ISO resolved to 2018-10-01
         * 2025-04-19 16:11:41.885	Thread-2	{},ISO resolved to 2018-10-01
         * 2025-04-19 16:11:41.885	Thread-0	{},ISO resolved to 2018-10-01
         * 2025-04-19 16:11:41.885	Thread-8	{},ISO resolved to 2018-10-01
         * 2025-04-19 16:11:41.885	Thread-7	{},ISO resolved to 2018-10-01
         * 2025-04-19 16:11:41.885	Thread-3	{},ISO resolved to 2018-10-01
         * 2025-04-19 16:11:41.885	Thread-4	{},ISO resolved to 2018-10-01
         * 2025-04-19 16:11:41.885	Thread-5	{},ISO resolved to 2018-10-01
         * 2025-04-19 16:11:41.885	Thread-6	{},ISO resolved to 2018-10-01
         * 2025-04-19 16:11:41.885	Thread-9	{},ISO resolved to 2018-10-01
         */
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                TemporalAccessor parse = dateTimeFormatter.parse("2018-10-01");
                Sout.d(parse.toString());
            }).start();
        }
    }
}
