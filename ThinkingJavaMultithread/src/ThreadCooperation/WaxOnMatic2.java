package ThreadCooperation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class car{
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private boolean waxOn = false;
    public void waxed(){
        lock.lock();
        try {
            waxOn = true;   //ready to buff
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }

    public void buffed() {
        lock.lock();
        try{
            waxOn = false;  //ready for another coat of wax
            condition.signalAll();

        }finally {
            lock.unlock();
        }


    }
    public void waitForWaxing() throws InterruptedException {
        lock.lock();
        try{
            while (waxOn == false){
                condition.await();
            }
        }finally {
            lock.unlock();
        }
    }
    public void waitForBuffing() throws InterruptedException {
        lock.lock();
        try{
            while (waxOn == true){
                condition.await();
            }
        }finally {
            lock.unlock();
        }

    }
}
class WaxOn1 implements Runnable{
    private Car car;
    WaxOn1(Car c){
        this.car = c;

    }
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()){
                System.out.println("wax on");
                TimeUnit.MILLISECONDS.sleep(2000);
                car.waxed();
                car.waitForBuffing();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("Ending wax on task");
    }
}
class WaxOff1 implements  Runnable{
    private Car car;
    WaxOff1(Car car){
        this.car = car;
    }
    @Override
    public void run() {
        try {
            while(!Thread.interrupted()){
                car.waitFoxWaxing();
                System.out.println("wax off");
                TimeUnit.MILLISECONDS.sleep(2000);
                car.buffed();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("Ending wax off task");
    }
}

public class WaxOnMatic2 {
    public static void main(String[] args) throws InterruptedException {
        Car car = new Car();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new WaxOff1(car));
        executorService.execute(new WaxOn1(car));
        TimeUnit.SECONDS.sleep(5);

        executorService.shutdownNow();
    }
}
