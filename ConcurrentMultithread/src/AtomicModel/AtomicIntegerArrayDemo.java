package AtomicModel;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicIntegerArrayDemo {
    private static AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(5);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < atomicIntegerArray.length(); i++) {
            atomicIntegerArray.set(i,1);
        }
        Thread t1 = new Thread(new AddFive());
        Thread t2 = new Thread(new Compare());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("All thread is finished .now value is ");
        for (int i = 0; i < atomicIntegerArray.length(); i++) {
            System.out.println(i+" - "+atomicIntegerArray.get(i));
        }

    }

    static class AddFive implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < atomicIntegerArray.length(); i++) {
                int addFive = atomicIntegerArray.addAndGet(i, 5);
                System.out.println("Thread " +Thread.currentThread().getId()+" /add five ,at "+i+" position value is "+addFive);
            }
            System.out.println("Thread " + Thread.currentThread().getId() + " /array now is : " + atomicIntegerArray);
        }
    }

    static class Compare implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < atomicIntegerArray.length(); i++) {
                boolean isFive = atomicIntegerArray.compareAndSet(i, 5, 3);
                System.out.println("Thread " + Thread.currentThread().getId() + " / comparing value to 5,result is " + isFive);
            }
            System.out.println("Thread " + Thread.currentThread().getId() + " /array now is : " + atomicIntegerArray);
        }
    }
}
