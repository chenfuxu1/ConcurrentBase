package htd.sharedmodelnolock.eight_unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-13 17:22
 * <p>
 * Desc: cas 操作
 */
public class TestUnSafe2 {
    private static Unsafe sUnsafe;

    /**
     * sun.misc.Unsafe@6d06d69c
     * Teacher{mId=100, mName='路飞'}1311053135
     */
    public static void main(String[] args) {
        try {
            Field unSafe = Unsafe.class.getDeclaredField("theUnsafe");
            unSafe.setAccessible(true);
            sUnsafe = (Unsafe) unSafe.get(null);
            System.out.println(sUnsafe);

            // 1. 获取域的偏移地址
            long idOffset = sUnsafe.objectFieldOffset(Teacher.class.getDeclaredField("mId"));
            long nameOffset = sUnsafe.objectFieldOffset(Teacher.class.getDeclaredField("mName"));
            Teacher teacher = new Teacher();

            // 2. 执行 cas 操作
            sUnsafe.compareAndSwapInt(teacher, idOffset, 0, 100);
            sUnsafe.compareAndSwapObject(teacher, nameOffset, null, "路飞");

            // 3. 验证
            System.out.println(teacher);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

class Teacher {
    volatile int mId;
    volatile String mName;

    @Override
    public String toString() {
        return "Teacher{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                '}' + hashCode();
    }
}
