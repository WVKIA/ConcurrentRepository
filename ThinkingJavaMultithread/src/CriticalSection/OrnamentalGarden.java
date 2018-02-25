package CriticalSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//计数类
class Count {
	private int count = 0;
	private Random rand = new Random(47);

	// 同步方法
	public synchronized int increment() {
		int temp = count;
		if (rand.nextBoolean()) {
			Thread.yield();
		}
		return (count = ++temp);
	}
	//同步方法获取值
	public synchronized int value() {
		return count;
	}
}

class Entrance implements Runnable {
	//通过一个静态的count对象对整体计数
	private static Count count = new Count();
	//静态list存放有效的Entrance对象
	private static List<Entrance> entrances = new ArrayList<>();
	private int number = 0;
	// doesn;t need synchronized to read
	//不需要同步方法进行修饰，因为已经可以是同步的
	private final int id;
	
	private static volatile boolean canceled = false;
	//在volatile对象进行，不需要同步，因为只在上进行读取和赋值，且是原子操作
	public static void cancele() {
		canceled = true;
	}

	public Entrance(int id) {
		this.id = id;
		// keep this task in a list
		// prevents garbage colllection of dead tasks
		entrances.add(this);

	}

	public void run() {
		while (!canceled) {
			synchronized (this) {
				++number;
			}
			System.out.println(this + " Tocal :" + count.increment());
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("sleep interrupted");
			}
		}
		System.out.println("stoping " + this);
	}

	public synchronized int getValue() {
		return number;
	}

	public String toString() {
		return "Entrance " + id + " : " + getValue();
	}

	public static int getTotalCount() {
		return count.value();
	}

	public static int sumEntrances() {
		int sum = 0;
		for (Entrance entrance : entrances) {
			sum += entrance.getValue();
		}
		return sum;
	}

}

public class OrnamentalGarden {
	public static void main(String[] args) throws InterruptedException {
		ExecutorService ex = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			ex.execute(new Entrance(i));
		}
		TimeUnit.SECONDS.sleep(1);
		//一秒钟之后发送cancled消息
		Entrance.cancele();
		//停止ex
		ex.shutdown();
		//等待每个任务结束，如果在超时时间内全部结束，返回true，否则返回false
		if (!ex.awaitTermination(250, TimeUnit.MILLISECONDS)) {
			System.out.println("some task were not terminated");
		}
		System.out.println("total " + Entrance.getTotalCount());
		System.out.println("sum of Entrances : " + Entrance.sumEntrances());
	}
}
