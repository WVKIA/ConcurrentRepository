package AtomicModel;

import java.util.concurrent.atomic.AtomicInteger;
/**
 * CAS
 * 比较交换
 * 通常是3个操作数，内存位置值V，旧的预期值A，新的值B，当且仅当预期值A和内存位置值V的值相同时，将内存位置值V的值改为B，否则什么都不做
 *
 * 会出现ABA问题
 * 在取出内存数据时，而和A进行比较的时间差内，有可能V已经变化，但仍是V，
 * 比如一个线程one从内存取出位置值V的值，这时候另一个线程也从内存取出值V，并且two修改内存值V变成B，然后又改成V，这时候线程one进行比较会发现相同，然后one成功；
 * 但是不代表这个是没有问题的，如果链表的头部发生两次改变并回去原值，不代表链表是没有变化的。
 *
 */


/**
 * 原子类Integer
 */
public class AtomicIntegerDemo {
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            Thread t=new Thread(new Runnable() {
                @Override
                public void run() {
//                    System.out.println(atomicInteger.getAndIncrement());
                    System.out.println(atomicInteger.addAndGet(1));
                }
            });
            t.start();

            //便于t执行完成
            t.join();
        }


    }
}
