package htd.sharedmodeltubeside.eight_wait_notify;


import htd.utils.Sout;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * Project: Concurrent
 * Create By: ChenFuXu
 * DateTime: 2022/9/18 21:44
 * <p>
 * 如果需要在多个类之间使用 GuardedObject 对象，作为参数传递不是很方便，因此设计一个用来解耦的中间类
 * 这样不仅能够解耦 结果等待者 和 结果生产者，还能够同时支持多个任务的管理
 * <p>
 * Futures 就好比居民楼一层的信箱（每个信箱有房间的编号），左侧的 t0，t2，t4 就好比等待邮件的居民
 * 右侧的 t1，t3 t5 就好比邮递员
 **/
public class GuardedObjectTest {
    /**
     * 2025-03-23 17:17:34.175	Thread-0	开始-居民 1 准备收信了
     * 2025-03-23 17:17:34.175	Thread-1	开始-居民 2 准备收信了
     * 2025-03-23 17:17:34.175	Thread-2	开始-居民 3 准备收信了
     * 2025-03-23 17:17:35.154	Thread-4	邮递员送信了，信箱为：2 内容为： 信件 2
     * 2025-03-23 17:17:35.154	Thread-3	邮递员送信了，信箱为：3 内容为： 信件 3
     * 2025-03-23 17:17:35.154	Thread-5	邮递员送信了，信箱为：1 内容为： 信件 1
     * 2025-03-23 17:17:35.155	Thread-1	结束-居民 2 收到信： 信件 2
     * 2025-03-23 17:17:35.155	Thread-2	结束-居民 3 收到信： 信件 3
     * 2025-03-23 17:17:35.155	Thread-0	结束-居民 1 收到信： 信件 1
     */
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new People().start();
        }

        Thread.sleep(1000);
        for (Integer id : MailBoxes.getIds()) {
            new Postman(id, " 信件 " + id).start();
        }
    }
}

/**
 * 居民类
 * 收信，
 */
class People extends Thread {
    @Override
    public void run() {
        GuardedObject2 guardedObject = MailBoxes.createGuardedObject();
        Sout.d("开始-居民 " + guardedObject.getId() + " 准备收信了");
        Object mail = guardedObject.get(5000);
        Sout.d("结束-居民 " + guardedObject.getId() + " 收到信：" + mail);
    }
}

/**
 * 邮递员类
 */
class Postman extends Thread {
    // 信箱的 id
    private int mId;
    // 信件的内容
    private String mMail;

    public Postman(int id, String mail) {
        this.mId = id;
        this.mMail = mail;
    }

    @Override
    public void run() {
        GuardedObject2 guardedObject = MailBoxes.getGuardedObject(mId);
        Sout.d("邮递员送信了，信箱为：" + guardedObject.getId() + " 内容为：" + mMail);
        guardedObject.complete(mMail);
    }
}

/**
 * 邮箱类
 */
class MailBoxes {
    // 因为这个 boxes 会被多线程访问，这里使用 Hashtable 线程安全
    private static Map<Integer, GuardedObject2> sBoxes = new Hashtable<>();
    private static int sId = 1;

    // 产生唯一的 id (防止线程安全的问题，需要加锁)
    private static synchronized int generateId() {
        return sId++;
    }

    // 创建 GuardedObject
    public static GuardedObject2 createGuardedObject() {
        GuardedObject2 guardedObject2 = new GuardedObject2(generateId());
        // 放进集合管理起来
        sBoxes.put(guardedObject2.getId(), guardedObject2);
        return guardedObject2;
    }

    // 获取所有的 id
    public static Set<Integer> getIds() {
        return sBoxes.keySet();
    }

    // 获取 GuardedObject
    public static GuardedObject2 getGuardedObject(int id) {
        // 因为获取完就不需要了，所以这里直接使用 remove 返回结果
        return sBoxes.remove(id);
    }
}

class GuardedObject2 {
    // 结果
    private Object mResponse;
    // 标识 GuardedObject
    private int mId;

    public GuardedObject2(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

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
