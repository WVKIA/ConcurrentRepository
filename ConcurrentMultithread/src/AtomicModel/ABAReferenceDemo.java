package AtomicModel;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 模拟ABA问题
 *
 */

/**
 * 解决ABA问题，可以通过AtomicMarkableReference或AtomicStampedReference
 */
public class ABAReferenceDemo {
    public final static AtomicReference<String> ATOMIC_REFERENCE = new AtomicReference<>("abc");

    public static void main(String[] args) {
        //比较交换设置数据
        //当改变数据为原先值的线程成功将数据改变，这时候就会有线程的compareAndSet成功，便又会改变数据，造成ABA
        for (int i = 0; i < 100; i++) {
            final int num = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(Math.abs((int) (Math.random() * 100)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (ATOMIC_REFERENCE.compareAndSet("abc", "abc2")) {
                        System.out.println("Thread "+num+" 获得对象并修改为 "+ATOMIC_REFERENCE.get());
                    }
                }
            }).start();
        }
        //线程用于改变数据为原先值
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!ATOMIC_REFERENCE.compareAndSet("abc2", "abc")) {

                }
                System.out.println("已经改为原始值 "+ATOMIC_REFERENCE.get());
            }
        }).start();
    }
}
