package AtomicModel;

import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicStampedReferenceDemo {
    public final static AtomicStampedReference<String> ATOMIC_STAMPED_REFERENCE = new AtomicStampedReference<>("abc",1);

    public static void main(String[] args) {
        //比较交换设置数据

        for (int i = 0; i < 100; i++) {
            final int num = i;
            //通过版本号的方式防止出现ABA
            final int stamp = ATOMIC_STAMPED_REFERENCE.getStamp();
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(Math.abs((int) (Math.random() * 100)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(ATOMIC_STAMPED_REFERENCE.getStamp());
                    if (ATOMIC_STAMPED_REFERENCE.compareAndSet("abc","abc1",stamp,stamp+1)) {
                        System.out.println(stamp);
                        System.out.println(ATOMIC_STAMPED_REFERENCE.getStamp());
                        System.out.println("Thread "+num+" 获得对象并修改为 "+ATOMIC_STAMPED_REFERENCE.getReference());
                    }
                }
            }).start();
        }
        //线程用于改变数据为原先值
        new Thread(new Runnable() {
            @Override
            public void run() {
                int stamp = ATOMIC_STAMPED_REFERENCE.getStamp();
                while (!ATOMIC_STAMPED_REFERENCE.compareAndSet("abc1", "abc",stamp,stamp+1)) {

                }
                System.out.println(stamp);
                System.out.println(ATOMIC_STAMPED_REFERENCE.getStamp());
                System.out.println("已经改为原始值"+ATOMIC_STAMPED_REFERENCE.getReference());
            }
        }).start();
    }
}
