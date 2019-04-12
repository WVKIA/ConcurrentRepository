package ReentrantLockModel;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用lock和condition实现生产者消费者
 */
class producerInt implements Runnable {
    //毒丸模型
    public static Integer POISON_PILL = new Integer(-1);
    private static AtomicInteger atomicInteger = new AtomicInteger();
    public static int MAX_SIZE = 9;

    private LinkedList<Integer> list;
    private Lock lock;
    private Condition fullCondition;
    private Condition emptyCondition;

    public producerInt(LinkedList<Integer> list, Lock lock, Condition fullCondition, Condition emptyCondition) {
        this.list = list;
        this.lock = lock;
        this.fullCondition = fullCondition;
        this.emptyCondition = emptyCondition;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            lock.lock();
            try {
                while (list.size() == MAX_SIZE) {
                    System.out.println(Thread.currentThread().getName() + " == 队列已满，等待消费== ");
                    fullCondition.await();
                }
                int temp = atomicInteger.getAndIncrement();
                if (temp == 100) {
                    list.add(POISON_PILL);
                    return;
                }
                list.add(temp);
                System.out.println(Thread.currentThread().getName() + " == 生产者生产 == " + temp);
                emptyCondition.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

    }
}

class consumerInt implements Runnable {
    private LinkedList<Integer> list;
    private Lock lock;
    private Condition fullCondition;
    private Condition emptyCondition;

    public consumerInt(LinkedList<Integer> list, Lock lock, Condition fullCondition, Condition emptyCondition) {
        this.list = list;
        this.lock = lock;
        this.fullCondition = fullCondition;
        this.emptyCondition = emptyCondition;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            lock.lock();
            try {
                while (list.size() == 0) {
                    System.out.println(Thread.currentThread().getName() + " 报告队列为空，等待生产 == ");
                    emptyCondition.await();
                }
                Integer temp = list.removeFirst();
                if (temp == producerInt.POISON_PILL) {
                    list.add(temp);
                    //注意此时的唤醒，由将要退出的线程唤醒其他线程，而不是由生产者唤醒，因为生产者已经退出了
                    emptyCondition.signalAll();
                    return;
                }
                System.out.println(Thread.currentThread().getName() + " ==  消费 == " + temp);
                fullCondition.signalAll();
                Thread.yield();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}

public class ReentrantLockPCModel {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition fullcondition = lock.newCondition();
        Condition emptyCondition = lock.newCondition();
        LinkedList<Integer> list = new LinkedList<>();
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(new producerInt(list, lock, fullcondition, emptyCondition));
        for (int i = 0; i < 5; i++) {
            service.execute(new consumerInt(list, lock, fullcondition, emptyCondition));
        }
        service.shutdown();
        while (!service.isTerminated()) {

        }

    }
}
