package CostumeAndProduce;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by wk on 2017/8/20.
 */

/**
 * 食物
 */
class  Meal{
    //订餐数目
    private  final  int orderNum;
    public  Meal(int orderNum){
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return "Meal "+orderNum;
    }
}

/**
 * 等待的人（消费者）
 */
class  waitPerson implements  Runnable{
    //餐馆
    private Restaurant restaurant;
    public waitPerson(Restaurant restaurant){
        this.restaurant = restaurant;
    }
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()){
               //同步代码块
                //如果餐馆的食物为null，一直等待，注意等待会释放掉锁
                synchronized (this){
                    while (restaurant.meal == null){
                        //wait() 会停止对应的线程，进入等待队列，直到被对应锁的notifyAll()唤醒，本例中也就是chief唤醒
                        wait();
                    }
                }
               //如果餐馆食物存在
                System.out.println("Waitperson got "+restaurant.meal+"=========================");
                //对餐馆的厨师进行同步锁定
                synchronized (restaurant.chef){
                    //设置食物喂null，代表吃掉了
                    restaurant.meal = null;
                    //唤醒厨师线程
                    restaurant.chef.notifyAll();
                }
            }
        }catch (InterruptedException e){
            System.out.println("Waiting person interrupted");
        }
    }
}

/**
 * 厨师（生产者）
 */
class  chef implements  Runnable{
    //餐馆
    private  Restaurant restaurant;
    private  int count = 0;
    public  chef(Restaurant r){
        this.restaurant = r;
    }
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()){
                //同步代码块
                synchronized (this){
                    //如果餐馆的 食物不为null，一直等待
                    while (restaurant.meal != null){
                        //wait() 会停止对应的线程，进入等待队列，直到被对应锁的notifyAll()唤醒
                        wait();
                    }

                }
                //如果count超过10，退出
                if(++count ==10){
                    System.out.println("Out of food.closing ");
                    restaurant.executorService.shutdownNow();
                }
                System.out.println("Order up!");
                //同步代码 锁定等待的顾客
                synchronized (restaurant.waitPerson){
                    //设置食物
                    restaurant.meal = new Meal(count);
                    //唤醒对应的等待顾客线程，因为调用notifyAll需要获取对应的锁
                    restaurant.waitPerson.notifyAll();
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        }catch (InterruptedException e){
            System.out.println("chef interrupted");
        }
    }
}

/**
 * 餐馆
 */
public class Restaurant {
    Meal meal;
    ExecutorService executorService = Executors.newCachedThreadPool();
    //使用同一个餐馆
    waitPerson waitPerson = new waitPerson(this);
    chef chef = new chef(this);
    public Restaurant(){
        executorService.execute(chef);
        executorService.execute(waitPerson);
    }

    public static void main(String[] args) {
        new Restaurant();
    }
}
