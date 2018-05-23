package CylicBarrierModel;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用场景：
 * 希望创建一组任务，并行执行，然后在下一个步骤之前全都等待，直到所有任务都完成，然后继续并行
 * <p>
 * 闭锁：用于等待事件，而栅栏是线程彼此等待
 */
class Horse implements Runnable {
    private static Random random = new Random(47);
    private CyclicBarrier cyclicBarrier;
    private CountDownLatch countDownLatch;
    private int finishedLine;
    private AtomicInteger traceNumber = new AtomicInteger(0);
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                if (traceNumber.get() >= finishedLine) {
                    System.out.println(Thread.currentThread().getName() + " won ");
                    break;
                }
                traceNumber.getAndAdd(random.nextInt(3));
                printTrace(traceNumber.get());
                cyclicBarrier.await();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        countDownLatch.countDown();
    }

    private void printTrace(int trace) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < trace; i++) {
            sb.append("=");
        }
        System.out.println(sb);
    }
}
public class CylicBarrierHorseRace {
    public static void main(String[] args) {

    }
}
