package PhilosopherEating;

/**
 * 筷子
 */
public class Chopstick {
    //是否被拿着
    private boolean tasken = false;

    //同步方法 拿筷子
    public synchronized void take() throws InterruptedException {
        //如果this始终被拿着，则等待
        while (tasken){
            wait();
        }
        //设置true，被拿
        tasken =true;
    }
    //同步方法 丢掉筷子
    public synchronized void drop(){
        tasken = false;
        //唤醒其他线程
        notifyAll();
    }
}
