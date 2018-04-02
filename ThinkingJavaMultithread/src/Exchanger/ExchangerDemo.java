package Exchanger;


import Semaphore.Fat;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

class ExchangerProducer<T> implements Runnable{
    private Generator<T> generator;
    private Exchanger<List<T>> exchanger;
    private List<T> holder;

    public ExchangerProducer( Exchanger<List<T>> exchanger, Generator<T> generator,List<T> holder) {
        this.generator = generator;
        this.exchanger = exchanger;
        this.holder = holder;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                for (int i = 0; i < ExchangerDemo.size; i++) {
                    holder.add(generator.next());   //给需要交换的对象添加
                }
                holder = exchanger.exchange(holder);    //调用exchange方法，阻塞等待另一个线程也调用exchange方法，然后两个线程交换数据
            }
        } catch (InterruptedException e) {
            System.out.println("InterruptedException exit   =======");
        }
    }
}

class ExchangerConsumer<T> implements Runnable {
    private Exchanger<List<T>> exchanger;
    private List<T> holder;
    private volatile T value;

    public ExchangerConsumer(Exchanger<List<T>> exchanger, List<T> holder) {
        this.exchanger = exchanger;
        this.holder = holder;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                holder = exchanger.exchange(holder);
                for (T t : holder) {
                    value = t;    //
                    holder.remove(t);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("IterruptedException exit =======");
        }
        System.out.println("Final value : "+value);
    }
}
public class ExchangerDemo {
    static int size=10;
    static int delay=5; //秒数

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Exchanger<List<Fat>> exchanger = new Exchanger<>();
        List<Fat> producerList = new CopyOnWriteArrayList<>();
        List<Fat> consumerList = new CopyOnWriteArrayList<>();
        executorService.execute(new ExchangerProducer<Fat>(exchanger, Basicgenrator.create(Fat.class), producerList));
        executorService.execute(new ExchangerConsumer<Fat>(exchanger, consumerList));
        TimeUnit.SECONDS.sleep(delay);
        executorService.shutdownNow();
    }
}
