package BlockingQueue;

import LiftOff.LiftOff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;

class LiftOffRunner implements Runnable{
    //关灯进程 同步队列.md
    private BlockingQueue<LiftOff> rockets;
    LiftOffRunner(BlockingQueue<LiftOff> queue){
        this.rockets = queue;
    }
    public void add(LiftOff lo){
        try {
            //将对应的对象放入
            rockets.put(lo);
        } catch (InterruptedException e) {
            System.out.println("Interrupted during put()");
        }
    }
    @Override
    public void run() {
     try {
         while (!Thread.interrupted()){
             //从同步队列中取出，并运行
             LiftOff rocket = rockets.take();
             rocket.run();
         }
     }catch (InterruptedException e){
         System.out.println("Waking from take()");
     }
        System.out.println("Exiting liftoffrunner");
    }
}
public class TestBlockingQueueLightOff {
    //获取控制台输入
    static void getKey(){
        try {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        }catch (IOException e){
            throw  new RuntimeException(e);
        }
    }
    static void getKey(String msg){
        System.out.println(msg);
        getKey();
    }
    //测试不同的阻塞队列
    static void test(String msg,BlockingQueue<LiftOff> queue){
        System.out.println(msg+"=======================");
        LiftOffRunner runner = new LiftOffRunner(queue);
        Thread t= new Thread(runner);
        t.start();
        for (int i = 0; i < 5; i++) {
            runner.add(new LiftOff(5));
        }
        getKey("Press enter ("+msg+")");
        t.interrupt();
        System.out.println("Finished "+msg+" test");
    }

    public static void main(String[] args) {
        test("LinkedBlockingQueue",new LinkedBlockingQueue<>());
        test("ArrayBlockingQueue",new ArrayBlockingQueue<>(3)); //fixed size
        test("SynchronousQueue",new SynchronousQueue<>());  //size of 1
    }
}
