package htd.sharedmodelnolock.three_atomic_ref;

import htd.utils.Sout;

import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * Project: ConcurrentBase
 * Create By: Chen.F.X
 * DateTime: 2025-04-13 7:48
 * <p>
 * Desc: AtomicMarkableReference
 */
public class TestMarkableReference {
    public static void main(String[] args) {
        GarbageBag bag = new GarbageBag("装满了垃圾");
        /**
         * 参数 2 mark 可以看作一个标记，表示垃圾袋满了
         */
        AtomicMarkableReference<GarbageBag> ref = new AtomicMarkableReference<>(bag, true);
        Sout.d("开始...");
        GarbageBag prev = ref.getReference();
        Sout.d(prev.toString());

        new Thread(() -> {
            Sout.d("保洁阿姨开始...");
            bag.setDesc("将垃圾袋清空了");
            ref.compareAndSet(bag, bag, true, false);
            Sout.d(bag.toString());
        }, "保洁阿姨").start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Sout.d("想换一只新的垃圾袋？");
        /**
         * prev: 旧的垃圾袋
         * new Gar：新的垃圾袋
         * expectedMark: 原来的标记
         * newMark：新的标记
         *
         * 已经有保洁阿姨更换了状态，这里更换失败，使用的还是用一个对象，同一个垃圾袋
         */
        boolean success = ref.compareAndSet(prev, new GarbageBag("空垃圾袋"), true, false);
        Sout.d("换了么？" + success);
        Sout.d(ref.getReference().toString());
    }
}


class GarbageBag {
    private String mDesc;

    public GarbageBag(String desc) {
        mDesc = desc;
    }

    public void setDesc(String desc) {
        this.mDesc = desc;
    }

    @Override
    public String toString() {
        return "GarbageBag{" +
                "mDesc='" + mDesc + '\'' +
                '}' + hashCode();
    }
}
