package SemaphoreModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 由Semaphore实现的对象池
 */
class getObjectTask implements Runnable{
    private SemaphoreObjectPool pool;

    public getObjectTask(SemaphoreObjectPool pool) {
        this.pool = pool;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Object o = pool.get();
                System.out.println(Thread.currentThread().getName()+" == 线程 == 获取对象 == "+o);
                TimeUnit.MILLISECONDS.sleep(500);
                pool.put(o);
                System.out.println(Thread.currentThread().getName() + " == 线程 == 放回对象 == " + o);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class SemaphoreObjectPool {
    private Semaphore semaphore;
    private List<Object> pool=new ArrayList<>();
    private final Lock lock = new ReentrantLock();

    private SemaphoreObjectPool(int size){
        semaphore = new Semaphore(size);
        for (int i = 0; i < size; i++) {
            pool.add(new String(i+""));
        }
    }

    /**
     *
     * @return
     * @throws InterruptedException 请求信号量发生中断需要自己处理
     */
    public Object get() throws InterruptedException {
        semaphore.acquire();
        return getObject();
    }

    private Object getObject() {
        Object o = null;
        lock.lock();
        try {
            if (pool != null && !pool.isEmpty()) {
                o=pool.remove(0);
            }
            return o;
        }finally {
            lock.unlock();
        }
    }

    public void put(Object o) {
        putObject(o);
        semaphore.release();
    }

    private void putObject(Object o) {
        lock.lock();
        try {
            pool.add(o);
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        SemaphoreObjectPool pool = new SemaphoreObjectPool(10);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            getObjectTask getObjectTask = new getObjectTask(pool);
            executorService.execute(getObjectTask);
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {

        }
    }
}
