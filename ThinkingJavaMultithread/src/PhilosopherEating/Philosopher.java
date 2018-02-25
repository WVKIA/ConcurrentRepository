package PhilosopherEating;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 哲学家
 */
public class Philosopher implements Runnable{
    //左筷子
    private Chopstick left;
    //右筷子
    private Chopstick right;
    private final int id;
    //思考因子
    private final int ponderFacter;

    private Random random = new Random(47);

    //暂停用作思考的时间
    private void pause() throws InterruptedException {
        if (ponderFacter == 0) return;
        TimeUnit.MILLISECONDS.sleep(random.nextInt(ponderFacter * 250));
    }

    public Philosopher(Chopstick left, Chopstick right, int id, int ponderFacter) {
        this.left = left;
        this.right = right;
        this.id = id;
        this.ponderFacter = ponderFacter;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()){
                System.out.println(this+" "+"Thinking");
                //思考
                pause();
                //philosopher becomes hungry
                System.out.println(this+" "+" rabbing right");
                //拿右筷子
                right.take();
                System.out.println(this+" "+"rabbing left");
                //拿左筷子
                left.take();
                System.out.println(this+" "+"eating");
                //吃饭
                pause();
                //丢掉右筷子
                right.drop();
                //丢掉左筷子
                left.drop();
            }
        }catch (InterruptedException e){
            System.out.println(this+" "+"eating via interrupted");
        }
    }

    @Override
    public String toString() {
        return "Philosopher "+ id;
    }
}
