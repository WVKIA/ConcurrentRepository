package ConcurrentPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * 马 类
 */
class Horse implements Runnable{
    private static int counter = 0;
    private final int id =counter++;
    private int strides = 0;
    private static Random random = new Random(47);
    //栅栏
    private static CyclicBarrier barrier;

    public Horse(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    //同步方法获取当前总共步数
    public synchronized int getStrides() {
        return strides;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()){
                synchronized (this){
                    strides += random.nextInt(3);
                }
                //阻塞直到所有任务都到达栅栏CycliBarrier
                barrier.await();
            }
        }catch (InterruptedException e){
            // A legitimate way to exit
        }catch (BrokenBarrierException e){
            //This one we want to know about
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Horse "+id +" ";
    }

    /**
     * 使用*号模拟当前马匹的总路程
     * @return
     */
    public String tracks(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < getStrides(); i++) {
            stringBuilder.append("*");
        }
        stringBuilder.append(id);
        stringBuilder.append("("+getStrides()+")");
        return stringBuilder.toString();
    }
}
public class HorseRace {
    //结束步数
    static final int FINISH_LINE = 75;
    private List<Horse> horses = new ArrayList<>();
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private CyclicBarrier barrier;

    public HorseRace(int nHorse,final int pause) {
        //栅栏动作作为匿名内部类创建
        barrier = new CyclicBarrier(nHorse, new Runnable() {
            @Override
            public void run() {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < FINISH_LINE; i++) {
                    stringBuilder.append("=");
                }
                System.out.println(stringBuilder);
                for (Horse h :
                        horses) {
                    System.out.println(h.tracks());
                }
                //打印每匹马当前步数
                for (Horse hourse :
                        horses) {
                    //如果有一匹等于或超过限制步数，获胜！结束所有任务
                    if (hourse.getStrides() >= FINISH_LINE){
                        System.out.println(hourse+" won!");
                        executorService.shutdownNow();
                        return;
                    }
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(pause);
                }catch (InterruptedException e){
                    System.out.println("barrier-action sleep interrupted");
                }
            }
        });
        for (int i = 0; i < nHorse; i++) {
            Horse horse = new Horse(barrier);
            horses.add(horse);
            executorService.execute(horse);
        }
    }

    public static void main(String[] args) {
        int nhouts = 7;
        int pause = 200;
        new HorseRace(nhouts,pause);
    }
}
