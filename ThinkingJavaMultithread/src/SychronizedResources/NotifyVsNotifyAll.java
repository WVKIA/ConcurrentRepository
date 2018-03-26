package SychronizedResources;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by wk on 2017/8/17.
 */

class Blocker {
    //同步代码块  等待呼叫
    synchronized void waitingCall() {
        try {
            while (!Thread.interrupted()) {
                //等待，释放锁，进入锁等待队列，直到notify或notifyAll唤醒
                wait();
                System.out.println(Thread.currentThread() + " ");

            }
        } catch (InterruptedException e) {
            // OK to exit this way
        }

    }
//唤醒
    synchronized void prod() {
        notify();

    }
//唤醒所有
    synchronized void prodAll() {
        notifyAll();
    }
}

/**
 * 任务类
 */
class Task implements Runnable {
    static Blocker blocker = new Blocker();

    //执行等待wait
    @Override
    public void run() {
        blocker.waitingCall();
    }
}

class Task2 implements Runnable {
    static Blocker blocker = new Blocker();

    /**
     * 执行等待wait
     */
    @Override
    public void run() {
        blocker.waitingCall();
    }
}

public class NotifyVsNotifyAll {
    public static void main(String[] args) throws InterruptedException {
        //执行器
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            executorService.execute(new Task());
        }
        executorService.execute(new Task2());
        //定时器
        Timer timer = new Timer();
        //定时器方法
        timer.scheduleAtFixedRate(new TimerTask() {
            boolean prod = true;

            @Override
            //交替执行notify和notifyAll
            public void run() {
                //如果true，唤醒notify
                if (prod) {
                    System.out.println("\nNotify()");
                    Task.blocker.prod();
                    prod = false;
                    //否则，唤醒notifyAll
                } else {
                    System.out.println("\nnotifyAll()");
                    Task.blocker.prodAll();
                    prod = true;
                }

            }
            //每0.4秒执行一次
        }, 400, 400); //run every 4 second
        TimeUnit.SECONDS.sleep(5);
        timer.cancel();//取消定时
        System.out.println("\nTime canceled");
        TimeUnit.MICROSECONDS.sleep(500);
        System.out.println("Task2.blocker.proAll()");
        //任务2唤醒all
        Task2.blocker.prodAll();
        TimeUnit.MILLISECONDS.sleep(500
            );
        System.out.println("\nShutting down");
        //停止所有线程
        executorService.shutdownNow(); // Interrupt all tasks
    }

}
