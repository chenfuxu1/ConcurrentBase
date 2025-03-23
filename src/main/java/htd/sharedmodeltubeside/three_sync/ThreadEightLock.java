package htd.sharedmodeltubeside.three_sync;

import htd.utils.Sout;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-03-22 11:31
 *
 * 线程八锁
 **/
public class ThreadEightLock {
    public static void main(String[] args) {
        // test1(); // 12 或者 21
        // test2();
        // test3();
        // test4();
        // test5();
        // test6();
        // test7();
        test8();
    }

    private static void test1() {
        Number1 number1 = new Number1();
        new Thread(number1::a, "t1").start();
        new Thread(number1::b, "t2").start();
    }

    public static void test2() {
        Number2 number2 = new Number2();
        new Thread(number2::a, "t1").start();
        new Thread(number2::b, "t2").start();
    }

    public static void test3() {
        Number3 number3 = new Number3();
        new Thread(number3::a, "t1").start();
        new Thread(number3::b, "t2").start();
        new Thread(number3::c, "t3").start();
    }

    /**
     * 情况4：锁住的是实例化对象
     * 所以，方法 a，b 执行的顺序是随机的，但调用的对方锁住的是两个对象
     * 所以互斥不成立，多核 CPU 并行执行，方法 a 有阻塞，所以先打印 2，才打印 1
     * 结果：21
     */
    public static void test4() {
        Number4 number4_1 = new Number4();
        Number4 number4_2 = new Number4();
        new Thread(number4_1::a, "4-1").start();
        new Thread(number4_2::b, "4-2").start();
    }

    /**
     * 情况5：方法 a 锁住的是静态方法，锁住的是类对象
     * 方法 b 锁住的是普通方法，锁住的是实例化对象，二者不是一个对象
     * 所以互斥不成立，多核 CPU 并行执行，方法 a 有阻塞，所以先打印 2，才打印 1
     * 结果：输出 2，1s 后输出 1
     */
    private static void test5() {
        Number5 number5 = new Number5();
        new Thread(Number5::a).start();
        new Thread(number5::b).start();
    }

    /**
     * 情况6：方法 a，b 锁住的是静态方法，锁住的是类对象
     * 线程 t1 t2 锁住的都是类对象，同一个对象，互斥成立
     * 结果：
     * 1、先执行 a，再执行 b，输出：1s 后输出 12
     * 2、先执行 b，再执行 a，输出：2, 1s 后输出 1
     */
    private static void test6() {
        Number6 number6 = new Number6();
        new Thread(Number6::a, "t1").start();
        new Thread(Number6::b, "t2").start();
    }

    /**
     * 情况7：方法 a 锁住的是静态方法，锁住的是类对象
     * 方法 b 锁住的是普通方法，锁住的是实例化对象，二者不是一个对象
     * 且方法 a 有延时
     * 结果：输出 2. 1s 后输出 1
     */
    private static void test7() {
        Number7 number7_1 = new Number7();
        Number7 number7_2 = new Number7();
        new Thread(() -> {number7_1.a();}, "t1").start();
        new Thread(number7_2::b, "t2").start();
    }

    /**
     * 情况8：方法 a  b 锁住的是静态方法，锁住的是类对象
     * 线程 t1 和 t2 的对象是 实例化对象，所以互斥不成立
     * 结果：
     * 1、2 1s 后，输出 1
     * 2、1s 后，输出 12
     */
    private static void test8() {
        Number8 n1 = new Number8();
        Number8 n2 = new Number8();
        new Thread(()->{ n1.a(); }, "t1").start();
        new Thread(()->{ n2.b(); }, "t2").start();
    }
}


/**
 * 情况1：锁住的是实例化对象
 * 所以，方法 a，b 执行的顺序是随机的
 * 结果：12 或者 21
 */
class Number1 {
    public synchronized void a() {
        Sout.d("1");
    }

    public synchronized void b() {
        Sout.d("2");
    }
}

/**
 * 情况2：锁住的是实例化对象，但方法 a 有 1s 的 sleep
 * 所以，方法 a，b 执行的顺序是随机的
 * 结果：
 * 1、先执行方法 a，再执行方法 b -> 1s 后，输出 12
 * 2、先执行方法 b，再执行方法 a -> 输出 2， 1s 后输出 1
 */
class Number2 {
    public synchronized void a() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Sout.d("1");
    }

    public synchronized void b() {
        Sout.d("2");
    }
}

/**
 * 情况3：锁住的是实例化对象，但方法 a 有 1s 的 sleep
 * 所以，方法 a，b, c 执行的顺序是随机的( c 没有锁，一定先输出)
 * 结果：
 * 1、先执行方法 c，再执行方法 b，a -> 输出 3，输出 2，1s 后输出 1
 * 2、先执行方法 b，再执行方法 a -> 输出 2 3， 1s 后输出 1
 * 3、先执行方法 a，再执行方法 b -> 输出 3， 1s 后输出 1 2
 */
class Number3 {
    public synchronized void a() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Sout.d("1");
    }

    public synchronized void b() {
        Sout.d("2");
    }

    public void c() {
        Sout.d("3");
    }
}


/**
 * 情况4：锁住的是实例化对象
 * 所以，方法 a，b 执行的顺序是随机的，但调用的对方锁住的是两个对象
 * 所以互斥不成立，多核 CPU 并行执行，方法 a 有阻塞，所以先打印 2，才打印 1
 * 结果：21
 */
class Number4 {
    public synchronized void a() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Sout.d("1");
    }

    public synchronized void b() {
        Sout.d("2");
    }
}

/**
 * 情况5：方法 a 锁住的是静态方法，锁住的是类对象
 * 方法 b 锁住的是普通方法，锁住的是实例化对象，二者不是一个对象
 * 所以互斥不成立，多核 CPU 并行执行，方法 a 有阻塞，所以先打印 2，才打印 1
 * 结果：输出 2，1s 后输出 1
 */
class Number5 {
    public static synchronized void a() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Sout.d("1");
    }

    public synchronized void b() {
        Sout.d("2");
    }
}


/**
 * 情况6：方法 a，b 锁住的是静态方法，锁住的是类对象
 */
class Number6 {
    public static synchronized void a() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Sout.d("1");
    }
    public static synchronized void b() {
        Sout.d("2");
    }
}

/**
 * 情况7：方法 a 锁住的是静态方法，锁住的是类对象
 * 方法 b 锁住的是普通方法，锁住的是实例化对象，二者不是一个对象
 */
class Number7 {
    public static synchronized void a() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Sout.d("1");
    }
    public synchronized void b() {
        Sout.d("2");
    }
}

/**
 * 情况8：方法 a  b 锁住的是静态方法，锁住的是类对象
 */
class Number8 {
    public static synchronized void a() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Sout.d("1");
    }
    public static synchronized void b() {
        Sout.d("2");
    }
}