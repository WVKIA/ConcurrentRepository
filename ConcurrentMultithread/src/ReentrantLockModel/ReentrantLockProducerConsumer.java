package ReentrantLockModel;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Product {
    int id;

    public Product(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "product " + id;
    }
}

class ProducerConsumerImpl {
    //最大容量
    private final int MAX = 9;
    private LinkedList<Product> list = new LinkedList<>();
    private Lock lock = new ReentrantLock();
    //满的条件变量
    private Condition full = lock.newCondition();
    //空的条件变量
    private Condition empty = lock.newCondition();

    public void put(Product product) {
        lock.lock();
        try {
            while (MAX == list.size()) {
                System.out.println("===INFO===：已满，等待消费...");
                full.await(); //满的condition等待
            }
            list.add(product);
            System.out.println("===INFO==：生产产品 "+product);
            empty.signalAll();  //唤醒消费condition
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("===ERROR===：中断");
        } finally {
            lock.unlock();
        }
    }


    public Product get() {
        Product product = null;
        lock.lock();
        try {
            while (list.isEmpty()) {
                System.out.println("===INFO===：空，"+Thread.currentThread().getName()+"等待生产");
                empty.await();
            }
            product = list.removeFirst();//取出一个进行消费
            System.out.println("===INFO==："+Thread.currentThread().getName()+" 消费了一个产品 "+product);
            full.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("===ERROR==：中断");
        }finally {
            lock.unlock();
        }
        return product;
    }

}

//生产者
class Producer implements Runnable {
    ProducerConsumerImpl producerConsumer;

    public Producer(ProducerConsumerImpl producerConsumer) {
        this.producerConsumer = producerConsumer;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            Product product = new Product(i);
            producerConsumer.put(product);
            //模拟生产时间消耗
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

//消费者
class Consumer implements Runnable {
    ProducerConsumerImpl producerConsumer;

    public Consumer(ProducerConsumerImpl producerConsumer) {
        this.producerConsumer = producerConsumer;
    }


    @Override
    public void run() {
        Product product = null;
        while ((product=producerConsumer.get()) != null){
            try {
                TimeUnit.MILLISECONDS.sleep((int) (Math.random() * 2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class ReentrantLockProducerConsumer {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ProducerConsumerImpl producerConsumer = new ProducerConsumerImpl();
        Producer producer = new Producer(producerConsumer);
        Consumer consumer1 = new Consumer(producerConsumer);
        Consumer consumer2 = new Consumer(producerConsumer);
        executorService.execute(producer);
        executorService.execute(consumer1);
        executorService.execute(consumer2);
        executorService.shutdown();

    }
}
