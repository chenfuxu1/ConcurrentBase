package htd.sharedmodeltool.juc.semaphore;

import htd.utils.Sout;

import java.util.concurrent.Semaphore;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-05-11 9:24
 * <p>
 * Desc:
 */
public class TestSemaphore {
    private static final String TAG = "TestSemaphore";

    /**
     * 2025-05-11 09:27:00.738	 t2		TestSemaphore	running...
     * 2025-05-11 09:27:00.738	 t1		TestSemaphore	running...
     * 2025-05-11 09:27:00.738	 t0		TestSemaphore	running...
     * 2025-05-11 09:27:01.747	 t1		TestSemaphore	end...
     * 2025-05-11 09:27:01.747	 t2		TestSemaphore	end...
     * 2025-05-11 09:27:01.747	 t0		TestSemaphore	end...
     * 2025-05-11 09:27:01.747	 t3		TestSemaphore	running...
     * 2025-05-11 09:27:01.747	 t5		TestSemaphore	running...
     * 2025-05-11 09:27:01.748	 t4		TestSemaphore	running...
     * 2025-05-11 09:27:02.763	 t3		TestSemaphore	end...
     * 2025-05-11 09:27:02.763	 t6		TestSemaphore	running...
     * 2025-05-11 09:27:02.763	 t4		TestSemaphore	end...
     * 2025-05-11 09:27:02.763	 t5		TestSemaphore	end...
     * 2025-05-11 09:27:02.763	 t7		TestSemaphore	running...
     * 2025-05-11 09:27:02.764	 t9		TestSemaphore	running...
     * 2025-05-11 09:27:03.778	 t6		TestSemaphore	end...
     * 2025-05-11 09:27:03.779	 t8		TestSemaphore	running...
     * 2025-05-11 09:27:03.779	 t7		TestSemaphore	end...
     * 2025-05-11 09:27:03.779	 t9		TestSemaphore	end...
     * 2025-05-11 09:27:04.792	 t8		TestSemaphore	end...
     * @param args
     */
    public static void main(String[] args) {
        // 1.创建 semaphore 对象, 参数表示最大线程数
        Semaphore semaphore = new Semaphore(3);

        // 2. 10 个线程同时运行
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                // 3.获取许可
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    Sout.d(TAG, "running...");
                    Thread.sleep(1000);
                    Sout.d(TAG, "end...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 4.释放许可
                    semaphore.release();
                }
            }, " t" + i).start();
        }
    }
}
