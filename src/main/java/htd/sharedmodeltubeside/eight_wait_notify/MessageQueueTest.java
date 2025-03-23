package htd.sharedmodeltubeside.eight_wait_notify;


import htd.utils.Sout;

import java.util.LinkedList;

/**
 * Project: Concurrent
 * Create By: ChenFuXu
 * DateTime: 2022/9/21 22:49
 * <p>
 * 消息队列类，java 线程之间通信
 **/

public class MessageQueueTest {
    /**
     * 2025-03-23 17:25:17.482	生产者 0	已生产消息：Message{mId=0, mValue=生产的消息0}
     * 2025-03-23 17:25:17.484	生产者 2	已生产消息：Message{mId=2, mValue=生产的消息2}
     * 2025-03-23 17:25:17.484	生产者 1	队列已满，生产者等待
     * 2025-03-23 17:25:18.471	消费者		已消费信息：Message{mId=0, mValue=生产的消息0}
     * 2025-03-23 17:25:18.471	生产者 1	已生产消息：Message{mId=1, mValue=生产的消息1}
     * 2025-03-23 17:25:19.479	消费者		已消费信息：Message{mId=2, mValue=生产的消息2}
     * 2025-03-23 17:25:20.483	消费者		已消费信息：Message{mId=1, mValue=生产的消息1}
     * 2025-03-23 17:25:21.485	消费者		队列为空，消费者等待
     */
    public static void main(String[] args) {
        MessageQueue messageQueue = new MessageQueue(2);
        // 生产者
        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                messageQueue.put(new Message(id, "生产的消息" + id));
            }, "生产者 " + id).start();
        }
        // 消费者
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    messageQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "消费者").start();
    }
}

class MessageQueue {
    // 消息的队列集合
    private LinkedList<Message> mList = new LinkedList<>();
    // 队列容量
    private int mCapacity;

    public MessageQueue(int capacity) {
        this.mCapacity = capacity;
    }

    // 获取消息
    public Message take() {
        // 检查对象是否为空
        synchronized (mList) {
            while (mList.isEmpty()) {
                try {
                    Sout.d("队列为空，消费者等待");
                    mList.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 从队列头部获取消息并返回
            Message message = mList.removeFirst();
            /**
             * 已经取走一个，可以通知生产者生产了，不然生产者以为队列满了
             * 会一直等待
             */
            Sout.d("已消费信息：" + message);
            mList.notifyAll();
            return message;
        }
    }

    // 存入消息
    public void put(Message message) {
        synchronized (mList) {
            // 检查对象是否已满
            while (mList.size() == mCapacity) {
                try {
                    Sout.d("队列已满，生产者等待");
                    mList.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 将消息加入队列尾部
            mList.addLast(message);
            /**
             * 现在有消息了，唤醒等待的线程
             * 不然等消息的线程一直获取不到消息
             */
            Sout.d("已生产消息：" + message);
            mList.notifyAll();
        }
    }


}

final class Message {
    private int mId;
    private Object mValue;

    public Message(int id, Object value) {
        mId = id;
        mValue = value;
    }

    public int getId() {
        return mId;
    }

    public Object getValue() {
        return mValue;
    }

    @Override
    public String toString() {
        return "Message{" +
                "mId=" + mId +
                ", mValue=" + mValue +
                '}';
    }
}
