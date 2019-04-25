package SemaphoreModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author wukai
 * @date 2019/4/24
 */
class RTask implements Runnable {
    private Semaphore semaphore;

    public RTask(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " 请求一个信号量");
        try {
            semaphore.acquire();
            TimeUnit.SECONDS.sleep(2);

            System.out.println(Thread.currentThread().getName() +" 释放一个信号量");
            semaphore.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
public class SemaphoreTest {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(5);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 20; i++) {
            executorService.execute(new RTask(semaphore));
        }
        executorService.shutdown();

        while (!executorService.isTerminated()) {

        }
    }
}
