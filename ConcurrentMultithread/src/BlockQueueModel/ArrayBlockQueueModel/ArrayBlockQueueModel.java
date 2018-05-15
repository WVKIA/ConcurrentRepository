package BlockQueueModel.ArrayBlockQueueModel;

import javax.xml.soap.Text;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生产者任务
 */
class produceInt implements Runnable {
    private BlockingQueue<Integer> blockingQueue;
    public static final Integer POISON_PILL = new Integer(-1);


    private static AtomicInteger integer = new AtomicInteger();

    public produceInt(BlockingQueue<Integer> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                int temp = integer.getAndIncrement();
                if (temp > 100) {
                    blockingQueue.put(POISON_PILL);
                    break;
                }
                blockingQueue.put(temp);
                System.out.println(Thread.currentThread().getName()+" ==  生产 =="+temp);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 消费者任务
 */
class consumerInt implements Runnable {
    private BlockingQueue<Integer> blockingQueue;

    public consumerInt(BlockingQueue<Integer> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Integer tmep=  blockingQueue.take();
                if (tmep == produceInt.POISON_PILL) {
                    blockingQueue.put(tmep);
                    break;
                }

                System.out.println(Thread.currentThread().getName() + " == 消费 == " + tmep);
                Thread.yield();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class ArrayBlockQueueModel {
    public static void main(String[] args) {
        //具有固定大小的阻塞队列
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(10);
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(new produceInt(blockingQueue));
        for (int i = 0; i < 5; i++) {
            service.execute(new consumerInt(blockingQueue));
        }
        service.shutdown();
        while (!service.isTerminated()) {
            Thread.yield();
        }
    }
}
