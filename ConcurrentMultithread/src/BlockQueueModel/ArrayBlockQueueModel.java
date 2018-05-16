package BlockQueueModel;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class ProducerTask<T> implements Runnable{
    private BlockingQueue<T> queue;
    private volatile  boolean isend;
    //计数
    private AtomicInteger count = new AtomicInteger();

    public boolean isIsend() {
        return isend;
    }

    public void setIsend(boolean isend) {
        this.isend = isend;
    }

    public ProducerTask(BlockingQueue<T> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                T data = (T) new Object();
                queue.put(data);
                System.out.println("===INFO==：线程 " + Thread.currentThread().getName() + " 生产 " + data);
                count.getAndIncrement();
                if (count.get() == 20) {
                    Thread.interrupted();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
class ConsumerTask<T> implements Runnable{
    private BlockingQueue<T> queue;

    public ConsumerTask(BlockingQueue<T> queue) {
        this.queue = queue;
    }
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                T data = queue.take();
                System.out.println("===INFO==：线程 " + Thread.currentThread().getName() + " 消费 " + data);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
public class ArrayBlockQueueModel {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        BlockingQueue queue = new ArrayBlockingQueue(1);
        ProducerTask<Integer> producerTask = new ProducerTask<>(queue);
        ConsumerTask<Integer> consumerTask = new ConsumerTask<>(queue);
        executorService.execute(producerTask);
        executorService.execute(consumerTask);
        executorService.shutdown();
    }
}
