package htd.sharedmodeltubeside.eight_wait_notify;

import htd.utils.Sout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-23 15:48
 **/
public class TestWaitAPI {
    private static final Object ROOM = new Object();
    private static boolean sHasCigarette = false;
    private static boolean sHasTakeout = false;

    public static void main(String[] args) {
        // test1();
        // test2();
        // test3();
        // test4();
        test5();
    }

    /**
     * 使用 sleep 方法的缺点
     * 1、其他干活的线程，需要一直阻塞，效率太低
     * 2、小南线程必须睡足 2s 才能醒来，就算烟提前送到，也无法立刻醒来
     * 3、加了 synchronized(ROOM) 就好比小南在里面反锁了们睡觉，烟根本没法送进门
     * main 没加 synchronized 就好像 main 线程是翻窗户进来的
     * 解决方法：使用 wait - notify 机制
     * <p>
     * 2025-03-23 15:49:36.362	小南		有烟吗：false
     * 2025-03-23 15:49:36.363	小南		现在没烟，先歇会
     * 2025-03-23 15:49:37.353	送烟人		已经拿到烟了
     * 2025-03-23 15:49:38.371	小南		现在有烟吗：true
     * 2025-03-23 15:49:38.371	小南		有烟，开始干活了
     * 2025-03-23 15:49:38.371	其他人 4	可以开始干活了
     * 2025-03-23 15:49:38.371	其他人 3	可以开始干活了
     * 2025-03-23 15:49:38.371	其他人 2	可以开始干活了
     * 2025-03-23 15:49:38.371	其他人 1	可以开始干活了
     * 2025-03-23 15:49:38.371	其他人 0	可以开始干活了
     */
    private static void test1() {
        new Thread(() -> {
            synchronized (ROOM) {
                Sout.d("有烟吗：" + sHasCigarette);
                if (!sHasCigarette) {
                    Sout.d("现在没烟，先歇会");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Sout.d("现在有烟吗：" + sHasCigarette);
                if (sHasCigarette) {
                    Sout.d("有烟，开始干活了");
                } else {
                    Sout.d("还是没烟，结束任务了");
                }
            }

        }, "小南").start();

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                synchronized (ROOM) {
                    Sout.d("可以开始干活了");
                }
            }, "其他人 " + i).start();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            /**
             * 这里能加 synchronized (ROOM) ?
             */
            sHasCigarette = true;
            Sout.d("已经拿到烟了");
        }, "送烟人").start();
    }

    /**
     * 解决方法：使用 wait - notify 机制
     * <p>
     * 2025-03-23 15:54:56.547	小南		有烟吗：false
     * 2025-03-23 15:54:56.548	小南		现在没烟，先歇会
     * 2025-03-23 15:54:56.548	其他人 4	可以开始干活了
     * 2025-03-23 15:54:56.548	其他人 3	可以开始干活了
     * 2025-03-23 15:54:56.548	其他人 2	可以开始干活了
     * 2025-03-23 15:54:56.548	其他人 1	可以开始干活了
     * 2025-03-23 15:54:56.548	其他人 0	可以开始干活了
     * 2025-03-23 15:54:57.540	送烟人		已经拿到烟了
     * 2025-03-23 15:54:57.540	小南		现在有烟吗：true
     * 2025-03-23 15:54:57.540	小南		有烟，开始干活了
     */
    private static void test2() {
        new Thread(() -> {
            synchronized (ROOM) {
                Sout.d("有烟吗：" + sHasCigarette);
                if (!sHasCigarette) {
                    Sout.d("现在没烟，先歇会");
                    try {
                        ROOM.wait(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Sout.d("现在有烟吗：" + sHasCigarette);
                if (sHasCigarette) {
                    Sout.d("有烟，开始干活了");
                } else {
                    Sout.d("还是没烟，结束任务了");
                }
            }

        }, "小南").start();

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                synchronized (ROOM) {
                    Sout.d("可以开始干活了");
                }
            }, "其他人 " + i).start();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            synchronized (ROOM) {
                sHasCigarette = true;
                Sout.d("已经拿到烟了");
                ROOM.notify();
            }

        }, "送烟人").start();
    }

    /**
     * 解决方法：使用 wait - notify 机制
     * notify 只能随机唤醒一个 WaitSet 中的线程，这时如果有其它线程也在等待，那么就可能唤醒不了正确的线
     * 程，称之为 虚假唤醒
     * 解决方法，改为 notifyAll
     *
     * 025-03-23 16:00:38.228	小南		有烟吗：false
     * 2025-03-23 16:00:38.228	小南		现在没烟，先歇会
     * 2025-03-23 16:00:38.228	小女		外卖送到没: false
     * 2025-03-23 16:00:38.228	小女		没外卖，先歇会！
     * 2025-03-23 16:00:39.220	送外卖的	外卖已经送到了
     * 2025-03-23 16:00:39.220	小南		现在有烟吗：false
     * 2025-03-23 16:00:39.220	小南		还是没烟，结束任务了
     */
    private static void test3() {
        new Thread(() -> {
            synchronized (ROOM) {
                Sout.d("有烟吗：" + sHasCigarette);
                if (!sHasCigarette) {
                    Sout.d("现在没烟，先歇会");
                    try {
                        ROOM.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Sout.d("现在有烟吗：" + sHasCigarette);
                if (sHasCigarette) {
                    Sout.d("有烟，开始干活了");
                } else {
                    Sout.d("还是没烟，结束任务了");
                }
            }

        }, "小南").start();

        new Thread(() -> {
            synchronized (ROOM) {
                Thread thread = Thread.currentThread();
                Sout.d("外卖送到没: " + sHasTakeout);
                if (!sHasTakeout) {
                    Sout.d("没外卖，先歇会！");
                    try {
                        ROOM.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Sout.d("外卖送到没: " + sHasTakeout);
                if (sHasTakeout) {
                    Sout.d("可以开始干活了");
                } else {
                    Sout.d("没干成活...");
                }
            }
        }, "小女").start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            synchronized (ROOM) {
                sHasTakeout = true;
                Sout.d("外卖已经送到了");
                ROOM.notify();
            }

        }, "送外卖的").start();
    }

    /**
     * 解决方法，改为 notifyAll
     *
     * 2025-03-23 16:04:48.695	小南		有烟吗：false
     * 2025-03-23 16:04:48.696	小南		现在没烟，先歇会
     * 2025-03-23 16:04:48.696	小女		外卖送到没: false
     * 2025-03-23 16:04:48.696	小女		没外卖，先歇会！
     * 2025-03-23 16:04:49.688	送外卖的	外卖已经送到了
     * 2025-03-23 16:04:49.689	小女		外卖送到没: true
     * 2025-03-23 16:04:49.689	小女		可以开始干活了
     * 2025-03-23 16:04:49.689	小南		现在有烟吗：false
     * 2025-03-23 16:04:49.689	小南		还是没烟，结束任务了
     *
     * 用 notifyAll 仅解决某个线程的唤醒问题，但使用 if + wait 判断仅有一次机会，一旦条件不成立，就没有重新
     * 判断的机会了
     * 解决方法，用 while + wait，当条件不成立，再次 wait
     */
    private static void test4() {
        new Thread(() -> {
            synchronized (ROOM) {
                Sout.d("有烟吗：" + sHasCigarette);
                if (!sHasCigarette) {
                    Sout.d("现在没烟，先歇会");
                    try {
                        ROOM.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Sout.d("现在有烟吗：" + sHasCigarette);
                if (sHasCigarette) {
                    Sout.d("有烟，开始干活了");
                } else {
                    Sout.d("还是没烟，结束任务了");
                }
            }

        }, "小南").start();

        new Thread(() -> {
            synchronized (ROOM) {
                Thread thread = Thread.currentThread();
                Sout.d("外卖送到没: " + sHasTakeout);
                if (!sHasTakeout) {
                    Sout.d("没外卖，先歇会！");
                    try {
                        ROOM.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Sout.d("外卖送到没: " + sHasTakeout);
                if (sHasTakeout) {
                    Sout.d("可以开始干活了");
                } else {
                    Sout.d("没干成活...");
                }
            }
        }, "小女").start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            synchronized (ROOM) {
                sHasTakeout = true;
                Sout.d("外卖已经送到了");
                ROOM.notifyAll();
            }

        }, "送外卖的").start();
    }

    /**
     * 2025-03-23 16:08:11.315	小南		有烟吗：false
     * 2025-03-23 16:08:11.315	小南		现在没烟，先歇会
     * 2025-03-23 16:08:11.315	小女		外卖送到没: false
     * 2025-03-23 16:08:11.315	小女		没外卖，先歇会！
     * 2025-03-23 16:08:12.301	送外卖的	外卖已经送到了
     * 2025-03-23 16:08:12.301	小女		外卖送到没: true
     * 2025-03-23 16:08:12.301	小女		可以开始干活了
     * 2025-03-23 16:08:12.301	小南		现在没烟，先歇会
     * 2025-03-23 16:08:13.304	送烟的		烟已经送到了
     * 2025-03-23 16:08:13.305	小南		现在有烟吗：true
     * 2025-03-23 16:08:13.305	小南		有烟，开始干活了
     */
    private static void test5() {
        new Thread(() -> {
            synchronized (ROOM) {
                Sout.d("有烟吗：" + sHasCigarette);
                while (!sHasCigarette) {
                    Sout.d("现在没烟，先歇会");
                    try {
                        ROOM.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Sout.d("现在有烟吗：" + sHasCigarette);
                if (sHasCigarette) {
                    Sout.d("有烟，开始干活了");
                } else {
                    Sout.d("还是没烟，结束任务了");
                }
            }

        }, "小南").start();

        new Thread(() -> {
            synchronized (ROOM) {
                Thread thread = Thread.currentThread();
                Sout.d("外卖送到没: " + sHasTakeout);
                if (!sHasTakeout) {
                    Sout.d("没外卖，先歇会！");
                    try {
                        ROOM.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Sout.d("外卖送到没: " + sHasTakeout);
                if (sHasTakeout) {
                    Sout.d("可以开始干活了");
                } else {
                    Sout.d("没干成活...");
                }
            }
        }, "小女").start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            synchronized (ROOM) {
                sHasTakeout = true;
                Sout.d("外卖已经送到了");
                ROOM.notifyAll();
            }

        }, "送外卖的").start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            synchronized (ROOM) {
                sHasCigarette = true;
                Sout.d("烟已经送到了");
                ROOM.notifyAll();
            }

        }, "送烟的").start();
    }
}
