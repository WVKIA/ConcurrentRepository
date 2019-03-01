package ConcurrentPackage.Semaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Pool<T> {
    private int size;
    private List<T> items = new ArrayList<>();
    private volatile boolean[] checkedout;
    private Semaphore available;    //计数信号量

    public Pool(Class<T> classObject, int size) {

        this.size = size;
        checkedout = new boolean[size];
        available = new Semaphore(size, true);
        //加载对象池
        for (int i = 0; i < size; i++) {
            try {
                items.add(classObject.newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    public T checkOut() throws InterruptedException {
        available.acquire();    //请求一个信号量
        return getItem();
    }

    public void checkIn(T x) {
        if (releaseItem(x)) {
            available.release();    //回收一个信号量
        }
    }
    //使用synchronized锁住共享资源
    private synchronized T getItem() {
        for (int i = 0; i < size; i++) {
            if (!checkedout[i]) {
                checkedout[i] = true;
                return items.get(i);
            }
        }
        return null;
    }

    private synchronized boolean releaseItem(T item) {
        int index = items.indexOf(item);
        if (index == -1) return false;
        if (checkedout[index]) {
            checkedout[index] = false;
            return true;
        }
        return false;
    }
}
