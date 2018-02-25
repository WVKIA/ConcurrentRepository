package ConcurrentPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * 延时任务
 */

/***
 * 实现Delayed接口，因为DelayQueue用于放置实现Delayed接口的对象
 */
class DelayedTask implements Runnable,Delayed{
    private static int counter = 0;
    private final int id = counter++;
    //延时时间（毫秒）
    private final int delta;
    //到期时间（纳秒）
    private final long trigger;
    //保存任务被创建的顺序
    protected static List<DelayedTask> sequence = new ArrayList<>();

    public DelayedTask(int delayInMilliseconds) {
        this.delta = delayInMilliseconds;
        //将当前时间加上到期时间（并将delta毫秒转纳秒）
        trigger =System.nanoTime()+ TimeUnit.NANOSECONDS.convert(delta,TimeUnit.MILLISECONDS);
        sequence.add(this);
    }

    //获取延时时间有多长
    //实现Delayed方法，先到期的最先被take()取出
    public long getDelay(TimeUnit unit) {
        return unit.convert(trigger - System.nanoTime(),TimeUnit.NANOSECONDS);
    }

    /**
     * 比较方法
     * @param o
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        DelayedTask that = (DelayedTask) o;
        if (trigger < that.trigger) return -1;
        if (trigger > that.trigger) return 1;
        return 0;
    }

    @Override
    public void run() {
        System.out.println(this+" ");
    }

    @Override
    public String toString() {
        return String.format("%1$-4d ",delta)+" Task "+id;
    }

    /**
     * 返回延时时间和id
     * @return
     */
    public  String summary(){
        return "( "+id+" : " +delta+" )";
    }
    //关闭所有任务
    public static class EndSentinel extends DelayedTask{
        private ExecutorService executorService;

        public EndSentinel(int delayInMilliseconds, ExecutorService executorService) {
            super(delayInMilliseconds);
            this.executorService = executorService;
        }

        /**
         * 打印出所有任务
         */
        @Override
        public void run() {
            for (DelayedTask dt :
                    sequence) {
                System.out.println(dt.summary()+"  ");
            }
            System.out.println();
            System.out.println(this+" calling shutdownnow()");
            //关闭
            executorService.shutdownNow();
        }
    }
}

/**
 * 消费任务类
 */
class DelayedTaskConsumer implements Runnable{
    private DelayQueue<DelayedTask> queue;

    public DelayedTaskConsumer(DelayQueue<DelayedTask> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()){
                //取出一个任务并运行
                queue.take().run();;    //run task with the current thread
            }
        }catch (InterruptedException e){
            //Acceptable way to exit
        }
        System.out.println("Finished DelayedTaskConsumer");
    }
}
public class DelayQueueDemo {
    public static void main(String[] args) {
        Random random = new Random(47);
        ExecutorService executorService = Executors.newCachedThreadPool();
        DelayQueue<DelayedTask> queue = new DelayQueue<>();
        //Fill with tasks that have random delays
        for (int i = 0; i < 20; i++) {
            queue.put(new DelayedTask(random.nextInt(5000)));
        }
        //Set the stopping point
        queue.add(new DelayedTask.EndSentinel(5000,executorService));
        executorService.execute(new DelayedTaskConsumer(queue));
    }
}
