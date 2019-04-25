package CylicBarrierModel;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wukai
 * @date 2019/4/24
 */
class RTask implements Runnable {
    private CyclicBarrier cyclicBarrier;
    public RTask(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread() + " 完成自己的任务");
        try {
            cyclicBarrier.await();
            System.out.println("继续自己的其他任务");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
public class CylicBarrierTest {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, new Runnable() {

            @Override
            public void run() {
                System.out.println("所有线程执行完成了，执行其他任务吧");
            }
        });
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 5; i++) {
            executorService.execute(new RTask(cyclicBarrier));
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {

        }
    }


}
