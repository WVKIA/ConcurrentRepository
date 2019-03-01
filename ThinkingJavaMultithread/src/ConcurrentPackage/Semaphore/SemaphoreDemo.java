package ConcurrentPackage.Semaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

class CheckoutTask<T> implements Runnable {
    private static int counter = 0;
    private final int id = counter++;
    private Pool<T> pool;

    public CheckoutTask(Pool<T> pool) {
        this.pool = pool;
    }

    @Override
    public String toString() {
        return "CheckoutTask  " + id + " ";
    }

    @Override
    public void run() {
        try {
            T item = pool.checkOut();   //迁出一个对象，代表使用
            System.out.println(this + " check out =====" + item);
            TimeUnit.SECONDS.sleep(1);
            System.out.println(this + " check in ======" + item);
            pool.checkIn(item);

        } catch (InterruptedException e) {

        }
    }
}
public class SemaphoreDemo {
    final static int SIZE=25;

    public static void main(String[] args) throws InterruptedException {
        final Pool<Fat> pool = new Pool<>(Fat.class, SIZE);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < SIZE; i++) {
            executorService.execute(new CheckoutTask<Fat>(pool));
        }
        System.out.println("All checkoutTask created=============================");
        List<Fat> list = new ArrayList<>();

        for (int i = 0; i < SIZE; i++) {
            Fat f = pool.checkOut();
            System.out.println(i + " : main() thread checked out ======");
            f.operation();
            list.add(f);
        }
        Future<?> blocked=executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //信号量会阻塞迁出checkout操作，因为所有的信号量都被使用完了
                    pool.checkOut();
                } catch (InterruptedException e) {
                    System.out.println("Checkout() Interrupted============================");
                }
            }
        });
        TimeUnit.SECONDS.sleep(2);
        blocked.cancel(true);   //破坏阻塞

        System.out.println("Checking in objects in " + list+"============");
        for (Fat fat : list) {
            pool.checkIn(fat);
        }
        for (Fat fat : list) {
            pool.checkIn(fat);  //第二次迁入会被忽视
        }
        executorService.shutdown();
    }
}
