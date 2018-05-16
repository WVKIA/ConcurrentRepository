package CountDownModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
/**
 * 实现一个容器，提供两个方法 add,size
 * 写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束
 *
 */
class ArrayTestList<T>{
    private List<T> list;
    private CountDownLatch latch;

    public ArrayTestList( CountDownLatch latch) {
        this.list = new ArrayList<>();
        this.latch = latch;
    }

    public synchronized void add(T data) {
        list.add(data);
        System.out.println(" == 放入元素 == "+data);
        if (list.size() == 5) {
            latch.countDown();
        }
    }

    public synchronized int size(){
        return list.size();
    }
}

class putTask implements Runnable{
    private ArrayTestList<Integer> list;

    public putTask(ArrayTestList<Integer> list) {
        this.list = list;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
    }
}

class monitorTask implements Runnable {
    private ArrayTestList<Integer> list;
    private CountDownLatch latch;

    public monitorTask(ArrayTestList<Integer> list, CountDownLatch latch) {
        this.list = list;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            latch.await();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(" == 集合达到数目，监控停止 ");
    }
}

public class MonitorCountDownLatch {
    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(1);

        ArrayTestList<Integer> list = new ArrayTestList<>(latch);

        service.execute(new putTask(list));
        service.execute(new monitorTask(list, latch));

        service.shutdown();

        while (!service.isTerminated()) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
