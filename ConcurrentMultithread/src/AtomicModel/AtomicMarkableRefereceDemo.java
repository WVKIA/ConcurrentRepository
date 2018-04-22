package AtomicModel;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class AtomicMarkableRefereceDemo {
    public final static AtomicMarkableReference<String> ATOMIC_MARKABLE_REFERENCE = new AtomicMarkableReference<>("abc",false);

    public static void main(String[] args) {
        //比较交换设置数据

        for (int i = 0; i < 100; i++) {
            final int num = i;

            //通过true/false的方式防止出现ABA
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(Math.abs((int) (Math.random() * 100)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(ATOMIC_MARKABLE_REFERENCE.isMarked());
                    if (ATOMIC_MARKABLE_REFERENCE.compareAndSet("abc","abc1",false,true)) {
                        System.out.println(ATOMIC_MARKABLE_REFERENCE.isMarked());
                        System.out.println("Thread "+num+" 获得对象并修改为 "+ ATOMIC_MARKABLE_REFERENCE.getReference());
                    }
                }
            }).start();
        }
        //线程用于改变数据为原先值
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(ATOMIC_MARKABLE_REFERENCE.isMarked());
                if (ATOMIC_MARKABLE_REFERENCE.compareAndSet("abc1", "abc",false,true)){
                    System.out.println("已经改为原始值"+ ATOMIC_MARKABLE_REFERENCE.getReference());
                }

            }
        }).start();
    }
}
