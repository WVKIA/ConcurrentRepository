package ReentrantReadWriteLock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ReadWriteList<T>{
    private List<T> list = new ArrayList<>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    public void add(T data) {
        writeLock.lock();
        try {
            list.add(data);
        }finally {
            writeLock.unlock();
        }
    }

    public T get(int index) {
        readLock.lock();
        try {
            return list.get(index);
        }finally {
            readLock.unlock();
        }
    }

    //不是线程安全的，因为size可能拿到过期值，但因为只存在一个线程写，size只会增大，所以可以避免掉出现
    //IllegalArguemntException
    public int size() {
        readLock.lock();
        try {
            return list.size();
        }finally {
            readLock.unlock();
        }
    }
}
class WriteTask implements Runnable{
    private ReadWriteList<Integer> list;

    public WriteTask(ReadWriteList<Integer> list) {
        this.list = list;
    }

    @Override
    public void run() {
        int data = new Random().nextInt(100);
        list.add(data);
        System.out.println("===INFO==：" + Thread.currentThread().getName() + " add data " + data);
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
class ReadTask implements Runnable{
    private ReadWriteList<Integer> list;

    public ReadTask(ReadWriteList<Integer> list) {
        this.list = list;
    }
    @Override
    public void run() {
        int index = new Random().nextInt(list.size());
        System.out.println("===INFO==：" + Thread.currentThread().getName() + " read data " + String.valueOf(list.get(index)));
    }
}
public class ReadWriteLockModel {

    public static void main(String[] args) {
        ReadWriteList<Integer> list = new ReadWriteList<Integer>();
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 2; i++) {
            executorService.execute(new WriteTask(list));
        }
        for (int i = 0; i < 10; i++) {
            executorService.execute(new ReadTask(list));
        }
        executorService.shutdown();
    }
}
