package htd.sharedmodeltubeside.eight_wait_notify;

import htd.utils.DownLoader;
import htd.utils.Sout;

import java.util.List;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-23 16:30
 * <p>
 * 测试保护性暂停
 **/
public class TestGuarded {
    public static void main(String[] args) {
        test1();
        // test2();
    }

    /**
     * 线程 1 等待线程 2 的执行结果
     * 2025-03-23 16:32:45.298	t2		开始执行下载
     * 2025-03-23 16:32:45.298	t1		等待 t2 的下载结果
     * 2025-03-23 16:32:46.162	t1		结果的大小是：3
     */
    private static void test1() {
        GuardedObject guardedObject = new GuardedObject();
        new Thread(() -> {
            // 等待结果
            Sout.d("等待 t2 的下载结果");
            List<String> list = (List<String>) guardedObject.get();
            Sout.d("结果的大小是：" + list.size());
        }, "t1").start();

        new Thread(() -> {
            Sout.d("开始执行下载");
            List<String> download = DownLoader.download();
            guardedObject.complete(download);
        }, "t2").start();
    }

    /**
     * 线程 3 等待线程 4 的执行结果
     */
    private static void test2() {
        GuardedObject guardedObject = new GuardedObject();
        new Thread(() -> {
            // 等待结果
            Sout.d("等待 t4 的下载结果");
            Object response = guardedObject.get(2000);
            Sout.d("结果是：" + response);
        }, "t3").start();

        new Thread(() -> {
            Sout.d("开始执行下载");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            guardedObject.complete(new Object());
        }, "t4").start();
    }

}

class GuardedObject {
    // 结果
    private Object mResponse;

    // 获取结果
    public Object get() {
        synchronized (this) {
            // 没有结果
            while (mResponse == null) {
                try {
                    // 一直等待
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return mResponse;
    }

    /**
     * 获取结果
     * 增加参数时长，超过时间就不等待了
     */
    public Object get(long timeOut) {
        synchronized (this) {
            // 开始等待的时间
            long start = System.currentTimeMillis();
            // 已经经历的时间
            long passedTime = 0;
            // 没有结果
            while (mResponse == null) {
                // 这一轮循环应该要等待的时间
                long waitTime = timeOut - passedTime;
                // 表明经历的时间大于了要等待的时间，退出循环，不再等待
                if (waitTime <= 0) {
                    break;
                }
                try {
                    this.wait(waitTime); // 防止虚假唤醒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 求得经历的时间
                passedTime = System.currentTimeMillis() - start;
            }
        }
        return mResponse;
    }

    // 产生结果
    public void complete(Object response) {
        synchronized (this) {
            // 给结果成员变量赋值
            this.mResponse = response;
            this.notifyAll();
        }
    }
}