package htd.sharedmodelnolock.six_atomic_updater;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-13 8:19
 * <p>
 * Desc:
 * 原子更新器
 * AtomicReferenceFieldUpdater // 域 字段
 * AtomicIntegerFieldUpdater
 * AtomicLongFieldUpdater
 * 利用字段更新器，可以针对对象的某个域（Field）进行原子操作，只能配合 volatile 修饰的字段使用，否则会出现异常
 * Exception in thread "main" java.lang.IllegalArgumentException: Must be volatile type
 */
public class TestAtomicReferenceUpdater {
    public static void main(String[] args) {
        Student student = new Student();
        // 字段更新器
        AtomicReferenceFieldUpdater<Student, String> updater = AtomicReferenceFieldUpdater.newUpdater(Student.class,
                String.class, "mName");
        System.out.println(updater.compareAndSet(student, null, "张三"));
        System.out.println(student);
    }
}

class Student {
    volatile String mName;

    @Override
    public String toString() {
        return "Student{" +
                "mName='" + mName + '\'' +
                '}' + hashCode();
    }
}
