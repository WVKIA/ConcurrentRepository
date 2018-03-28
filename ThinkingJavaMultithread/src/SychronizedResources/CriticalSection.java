package SychronizedResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//not thread-safe
class Pair{
    private int x,y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pair() {
        this(0, 0);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public void incrementX() {
        x++;
    }
    public void incrementY(){
        y++;
    }

    @Override
    public String toString() {
        return "x : " + x + " y :  " + y;
    }

    public class PairValueNotEqualException extends RuntimeException {
        public PairValueNotEqualException() {
            super("pair values not equals : " + Pair.this);
        }
    }
    //检查状态 x和y是否相等
    public void checkStatus() {
        if (x != y) {
            throw new PairValueNotEqualException();
        }
    }
}
abstract class PairManager{
    AtomicInteger checkCounter = new AtomicInteger(0);
    protected Pair p = new Pair();
    private List<Pair> storage = Collections.synchronizedList(new ArrayList<>());
    public synchronized Pair getPair() {
        //采用新建备份的形式，保持原初对象的安全
        return new Pair(p.getX(), p.getY());
    }

    //保存，并表示一个时间消耗
    protected void store(Pair p) {
        storage.add(p);
        try {
            TimeUnit.MICROSECONDS.sleep(50);

        } catch (InterruptedException e) {

        }
    }

    public abstract void increment();

}

//使用synchronized进行同步
class PairManager1 extends PairManager {
    @Override
    public synchronized void increment() {
        p.incrementX();
        p.incrementY();
        store(getPair());
    }
}

//使用临界区
class PairManager2 extends PairManager {
    @Override
    public void increment() {
        Pair temp;
        synchronized (this) {
            p.incrementX();
            p.incrementY();
            temp = getPair();
        }
        store(temp);
    }
}

class PairManipulator implements Runnable {
    private PairManager pairManager;

    public PairManipulator(PairManager pairManager) {
        this.pairManager = pairManager;
    }

    @Override
    public void run() {
        while (true) {
            pairManager.increment();
        }

    }

    @Override
    public String toString() {
        return "Pair: " + pairManager.getPair() + " checkCounter = " + pairManager.checkCounter.get();
    }
}

class PairChecker implements Runnable {
    private PairManager pairManager;

    public PairChecker(PairManager pairManager) {
        this.pairManager = pairManager;
    }

    @Override
    public void run() {
        while (true) {
            pairManager.checkCounter.incrementAndGet();
            pairManager.getPair().checkStatus();

        }
    }
}
public class CriticalSection {
    //测试两个不同的任务
    static void testApproaches(PairManager pairManager1, PairManager pairManager2) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        PairManipulator pm1 = new PairManipulator(pairManager1), pm2 = new PairManipulator(pairManager2);
        PairChecker pcheck1 = new PairChecker(pairManager1), pcheck2 = new PairChecker(pairManager2);
        executorService.execute(pm1);
        executorService.execute(pm2);
        executorService.execute(pcheck1);
        executorService.execute(pcheck2);

        try {
            TimeUnit.MILLISECONDS.sleep(5000);

        } catch (InterruptedException e) {
            System.out.println("sleep interrupted");
        }
        System.out.println("pm1: " + pm1 + " pm2 : " + pm2);
    }

    public static void main(String[] args) {
        PairManager pairManager1 = new PairManager1(), pairManager2 = new PairManager2();
        testApproaches(pairManager1,pairManager2);
    }
}
