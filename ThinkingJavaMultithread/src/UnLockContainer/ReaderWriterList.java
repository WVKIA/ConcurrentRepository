package UnLockContainer;

import com.sun.glass.ui.Size;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁
 * @param <T>
 */
public class ReaderWriterList<T> {
    private ArrayList<T> lockedList;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public ReaderWriterList(int size,T initvalue) {
        lockedList = new ArrayList<>(Collections.nCopies(size, initvalue));
    }

    public T set(int index, T element) {
        Lock wlock = lock.writeLock();
        wlock.lock();
        try {
            return lockedList.set(index, element);
        }finally {
            wlock.unlock();
        }
    }

    public T get(int index) {
        Lock rlock = lock.readLock();
        rlock.lock();
        try {
            if (lock.getReadLockCount() > 1) {
                System.out.println(lock.getReadLockCount());
            }
            return lockedList.get(index);
        }finally {
            rlock.unlock();
        }
    }

    public static void main(String[] args) {
        new ReaderWriterTest(30, 1);
    }
}
class ReaderWriterTest{
    ExecutorService executorService = Executors.newCachedThreadPool();
    private final static int SIZE=100;
    private static Random random = new Random(47);
    private ReaderWriterList list = new ReaderWriterList(SIZE, 0);

    private class Writer implements Runnable {
        @Override
        public void run() {
            try {
                for (int i = 0; i < 20; i++) {
                    list.set(i, random.nextInt());
                    TimeUnit.MILLISECONDS.sleep(100);

                }
            } catch (InterruptedException e) {

            }
            System.out.println("Writer finished,shutting down");
            executorService.shutdownNow();
        }
    }

    private class Reader implements Runnable {
        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    for (int i = 0; i < SIZE; i++) {
                        list.get(i);
                        TimeUnit.MILLISECONDS.sleep(1);
                    }
                }
            } catch (InterruptedException e) {

            }
        }
    }

    public ReaderWriterTest(int re, int wr) {
        for (int i = 0; i < re; i++) {
            executorService.execute(new Reader());
        }
        for (int i = 0; i < wr; i++) {
            executorService.execute(new Writer());
        }
    }
}