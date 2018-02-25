package BlockingQueue;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/***
 * 管道是一个阻塞队列
 *对于从管道中读取，调用read()时，如果没有数据传入，管道会阻塞直到数据过来
 * PipedReader是可中断的
 * /

/**
 * 发送管道线程，用于写入数据
 */
class Sender implements Runnable{
    private Random random = new Random(47);
    /**
     * 允许任务向管道写
     */
    private PipedWriter out = new PipedWriter();
    public PipedWriter getPipedWriter() {
        return out;
    }

    @Override
    public void run() {
        try {
            while (true){
                //从A到z全部写入进去
                for (char c = 'A';c <= 'z';c++){
                    out.write(c);
                    TimeUnit.MILLISECONDS.sleep(random.nextInt(500));
                }
            }
        }catch (IOException e){
            System.out.println(e+" sender writer exception");
        }catch (InterruptedException e){
            System.out.println(e +" sender sleep interrupted");
        }
    }
}

/**
 * 接收线程，从管道中读取
 */
class Receiver implements Runnable{
    /**
     * 允许不同任务从同一个管道中读取
     */
    private PipedReader pipedIn;

    /**
     * 设置读取管道  为 对应的接收管道
     * @param sender
     * @throws IOException
     */
    public Receiver(Sender sender) throws IOException {
        pipedIn = new PipedReader(sender.getPipedWriter());
    }

    @Override
    public void run() {
        try {
            while (true){
                //读取数据从管道中
                System.out.println("Read : "+(char) pipedIn.read()+" ,");
            }
        }catch (IOException e){
            System.out.println(e+" receiver read exception");
        }
    }
}
public class PipedIO {
    public static void main(String[] args) throws InterruptedException, IOException {
        //写入管道
        Sender sender = new Sender();
        //接收管道为对应的写入管道流
        Receiver receiver = new Receiver(sender);
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(sender);
        executorService.execute(receiver);
        TimeUnit.SECONDS.sleep(5);
        executorService.shutdownNow();
    }
}
