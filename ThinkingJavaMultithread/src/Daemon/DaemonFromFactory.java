package Daemon;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 线程factory，用来对提交的线程进行特殊处理
 */
 class DaemonThreadFactory implements ThreadFactory {

     //实现这个方法，就是对所有提交到这个factory的线程进行设置
    @Override
    public Thread newThread(Runnable r) {
        Thread t= new Thread(r);
        t.setDaemon(true);
        return t;
    }

}

public class DaemonFromFactory implements Runnable {

    @Override
    public void run() {
        try {
            while (true) {
                TimeUnit.MILLISECONDS.sleep(100);
                System.out.println(Thread.currentThread() + " " + this);
            }
        } catch (Exception e) {
            System.out.println("interrupted");
        }
    }

    public static void main(String[] args) throws InterruptedException {

        //ExecutorService接收一个ThreadFactory对象用来创建线程
        ExecutorService excu = Executors.newCachedThreadPool(new DaemonThreadFactory());
        for (int i = 0; i < 5; i++) {
            excu.execute(new DaemonFromFactory());
        }
        System.out.println("All daemons started");
        TimeUnit.MILLISECONDS.sleep(500);
    }

}
