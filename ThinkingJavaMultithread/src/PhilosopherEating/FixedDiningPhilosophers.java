package PhilosopherEating;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedDiningPhilosophers {
    public static void main(String[] args) throws IOException {
        int ponder = 0; //size == 0 lock will be happen in time
        int size = 3;
        ExecutorService executorService = Executors.newCachedThreadPool();
        Chopstick[] sticks = new Chopstick[size];
        for (int i = 0; i < size; i++) {
            sticks[i] = new Chopstick();
        }

        for (int i = 0; i < size; i++) {
           if(i < size -1){
               //前几个都是同一边
               executorService.execute(new Philosopher(sticks[i],sticks[(i+1)],i,ponder));

           }else {
               //最后一个先拿反方向
               executorService.execute(new Philosopher(sticks[0],sticks[i],i,ponder));

           }
        }
        System.out.println("Press enter to quit");
        System.in.read();
        executorService.shutdownNow();
    }
}
