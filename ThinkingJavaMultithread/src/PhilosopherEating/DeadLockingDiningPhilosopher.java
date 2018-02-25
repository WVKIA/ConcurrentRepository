package PhilosopherEating;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeadLockingDiningPhilosopher {
    public static void main(String[] args) throws IOException {
        int ponder = 0; //size == 0 lock will be happen in time
        int size = 3;
        ExecutorService executorService = Executors.newCachedThreadPool();
        Chopstick[] sticks = new Chopstick[size];
        for (int i = 0; i < size; i++) {
            sticks[i] = new Chopstick();
        }

        for (int i = 0; i < size; i++) {
            //每个人都先拿起自己右边的，再拿起自己左边的
            executorService.execute(new Philosopher(sticks[i],sticks[(i+1)%size],i,ponder));
        }
        System.out.println("Press enter to quit");
        System.in.read();
        executorService.shutdownNow();
    }
}
