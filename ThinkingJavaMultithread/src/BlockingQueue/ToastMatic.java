package BlockingQueue;

import javax.management.JMException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * the class of Toast
 */

/**
 * 吐司
 */
class Toast {
    //枚举类
    public enum Status {
        DRY, BUTTERED, JAMMED
    }

    private Status status = Status.DRY;
    private final int id;

    public Toast(int id) {
        this.id = id;
    }

    public void butter() {
        status = Status.BUTTERED;
    }

    public void jam() {
        status = Status.JAMMED;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Toast " + id + " : " + status;
    }
}

/**
 * ToastQueue implements the LinkedBlockingQueue
 */
//实现LinkedBlockingQueue
class ToastQueue extends LinkedBlockingQueue<Toast> {
}

/**
 * the person of making kinds of toast
 */
class Toaster implements Runnable {
    private ToastQueue toastQueue;
    private int count = 0;
    private Random random = new Random(47);

    public Toaster(ToastQueue toastQueue) {
        this.toastQueue = toastQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(100 + random.nextInt(500));
                //Make toast
                //制作吐司
                Toast t = new Toast(count++);
                System.out.println(t);
                //Insert into queue
                //插入到对应的队列
                toastQueue.put(t);
            }
        } catch (InterruptedException e) {
            System.out.println("Toaster interrupted");
        }
        System.out.println("Toaster off");
    }

}

//Apply butter to toast
class Butterer implements Runnable{
    //对应的阻塞队列
    private ToastQueue dryQueue,butteredQueue;
    public Butterer(ToastQueue dryQueue,ToastQueue butteredQueue){
        this.dryQueue = dryQueue;
        this.butteredQueue = butteredQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()){
                //Blocks until next piece of toast is available;
                //取出对应的吐司
                Toast t = dryQueue.take();
                //摸黄油
                t.butter();
                System.out.println(t);
                //放入黄油吐司队列
                butteredQueue.put(t);
            }
        }catch (InterruptedException e){
            System.out.println("Butterer interrupted");
        }
        System.out.println("Butterer off");
    }
}

//Applay jam to buttered toast
class Jammer implements Runnable{
    private ToastQueue butteredQueue,finishedQueue;

    public Jammer(ToastQueue butteredQueue, ToastQueue finishedQueue) {
        this.butteredQueue = butteredQueue;
        this.finishedQueue = finishedQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()){
                //Blocks until next piece of toast is available;
                //从黄油队列中取出吐司
                Toast t = butteredQueue.take();
                //抹上果酱
                t.jam();
                System.out.println(t);
                //放入完成队列
                finishedQueue.put(t);
            }
        }catch (InterruptedException e){
            System.out.println("Jammer interrupted");
        }
        System.out.println("Jammer off");
    }

}
//Consume the toast
class Eater implements Runnable{
    private ToastQueue finishedQueue;
    private int count =0;

    public Eater(ToastQueue finishedQueue) {
        this.finishedQueue = finishedQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()){
                //blocks until next piece of toast is available
                //从完成队列取出对应的吐司，如果没有则阻塞
                Toast t = finishedQueue.take();
                //Verify that the taost is comming in order
                //and that all pieces are getting jammed
                if (t.getId() != count++ || t.getStatus() != Toast.Status.JAMMED){
                    System.out.println(">>Error : "+t);
                    System.exit(1);
                }else {
                    System.out.println("Chomp "+t+" ==================");
                }
            }
        }catch (InterruptedException e){
            System.out.println("Eater interrupted");
        }
        System.out.println("Eater off");
    }
}
public class ToastMatic {
    public static void main(String[] args) throws InterruptedException {
        ToastQueue dryQueue = new ToastQueue(),butteredQueue = new ToastQueue(),finishedQueue = new ToastQueue();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Toaster(dryQueue));
        executorService.execute(new Butterer(dryQueue,butteredQueue));
        executorService.execute(new Jammer(butteredQueue,finishedQueue));
        executorService.execute(new Eater(finishedQueue));
        TimeUnit.SECONDS.sleep(5);
        executorService.shutdownNow();
    }
}
