package htd.sharedmodelnolock.eight_unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-13 9:18
 * <p>
 * Desc: Unsafe 对象提供了非常底层的，操作内存、线程的方法，Unsafe 对象不能直接调用，只能通过反射获得
 */
public class TestUnSafe {
    private static Unsafe sUnsafe;

    public static void main(String[] args) {
        try {
            Field unSafe = Unsafe.class.getDeclaredField("theUnsafe");
            unSafe.setAccessible(true);
            sUnsafe = (Unsafe) unSafe.get(null);
            System.out.println(sUnsafe); // sun.misc.Unsafe@6d06d69c
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
