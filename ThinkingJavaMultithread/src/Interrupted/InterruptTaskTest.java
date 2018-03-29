package Interrupted;

class ATask implements Runnable {
    private double d = 0.0;
    @Override
    public void run() {
        //死循环打印i'm running和做消耗时间的浮点数计算
        while (true) {
            System.out.println("I'm running...........");
            for (int i = 0; i < 9000; i++) {
                d = d + (Math.PI + Math.E) / d;
            }
            //给线程调度器可以分配给其他线程的信号
            Thread.yield();
        }
    }
}
public class InterruptTaskTest {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new ATask());
        t.start();
        //运行一段时间
        Thread.sleep(10);
        System.out.println("********************************");
        System.out.println("Interrupted Thread！");
        System.out.println("======");
        t.interrupt();
    }
}
