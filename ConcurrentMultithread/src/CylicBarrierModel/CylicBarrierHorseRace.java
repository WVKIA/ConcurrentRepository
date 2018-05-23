package CylicBarrierModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private AtomicInteger traceNumber = new AtomicInteger(0);

    public Horse(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                traceNumber.getAndAdd(random.nextInt(3));
                printTrace(traceNumber.get());
                cyclicBarrier.await();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printTrace(int trace) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < trace; i++) {
            sb.append("*");
        }
        System.out.println(Thread.currentThread().getName()+""+sb);
    }

    public int getTraceNumber() {
        return traceNumber.get();
    }
}

class HorseCylic implements Runnable {
    private List<Horse> horseList;
    private CountDownLatch countDownLatch;
    private ExecutorService executorService;
    private int FINISHLINE=54;

    public HorseCylic(List<Horse> horseList, CountDownLatch countDownLatch, ExecutorService executorService) {
        this.executorService = executorService;
        this.horseList = horseList;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        System.out.println("栅栏动作处理。。。");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < Thread.currentThread().getName().length(); i++) {
            stringBuilder.append(" ");
        }
        for (int i = 0; i < FINISHLINE; i++) {
            stringBuilder.append("=");
        }
        stringBuilder.append("||");
        System.out.println(stringBuilder.toString());
        for (Horse horse : horseList) {
            if (horse.getTraceNumber() >= FINISHLINE) {
                System.out.println(horse+" won !");
                countDownLatch.countDown();
                executorService.shutdownNow();
                return;
            }
        }
    }
}
public class CylicBarrierHorseRace {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        int size=5;
        List<Horse> horses = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        HorseCylic horseCylic = new HorseCylic(horses, countDownLatch,executorService);

        CyclicBarrier cyclicBarrier = new CyclicBarrier(5,horseCylic);
        for (int i = 0; i < size; i++) {
            Horse horse = new Horse(cyclicBarrier);
            horses.add(horse);
            executorService.execute(horse);
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("比赛结束。。。");
    }
}
