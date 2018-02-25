package ConcurrentPackage;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CountDown被用来同步一个或多个任务，强制它们等待由其他任务执行的一组操作完成
 * 任何调用CountDownLatch.wait() 的线程都会阻塞，直到countDownLatch的计数为0
 * 其他任务可以调用countDown()来减小计数
 * 计数只能被设置一次
 */
//Performs some portion of a task
class TaskPortion implements Runnable{
    private static int counter = 0;
    private final int id =counter++;
    private static Random random = new Random(47);
    private final CountDownLatch latch;

    //设置countDownLatch
    public TaskPortion(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            dowork();
            //计数减一
            latch.countDown();
        }catch (InterruptedException e){
            //Acceptable way to exit
        }
    }
    //睡眠模仿工作
    public void dowork() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(random.nextInt(2000));
        System.out.println(this+" completed");
    }

    @Override
    public String toString() {
        return String.format("%1$-3d ",id);
    }
}
//waits on the COuntDownlatch
//等待线程
class WaitingTask implements Runnable{
    private static int counter = 0;
    private final int id =counter++;
    private final CountDownLatch latch;

    //设置countDownLatch
    public WaitingTask(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            //调用await()，此线程会阻塞直到countDownLatch计数为0
            latch.await();
            System.out.println("Latch barrider pased for "+this);
        }catch (InterruptedException e){
            System.out.println(this+" interrupted");
        }
    }

    @Override
    public String toString() {
        return String.format("WaitingTaks : %1$-3d ",id);
    }
}
public class CountDownLatchDemo {
    static final int SIZE= 100;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        //All must share a single CountDownLatch object
        //所有线程countDownLatch必须使用同一个对象
        CountDownLatch latch = new CountDownLatch(SIZE);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new WaitingTask(latch));
        }
        for (int i = 0; i < SIZE; i++) {
            executorService.execute(new TaskPortion(latch));
        }
        System.out.println("Launched all tasks");
        executorService.shutdown(); //quit when all tasks complete
    }
}
