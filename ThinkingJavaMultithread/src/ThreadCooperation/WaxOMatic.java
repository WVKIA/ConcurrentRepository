package ThreadCooperation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 汽车类
 */
class Car{
	/**
	 * 是否正在打蜡
	 */
	private boolean waxOn = false;

	/**
	 * 打蜡工作
	 */
	public synchronized void waxed(){
		waxOn = true;
		notifyAll();
		
	}

	/**
	 * 抛光工作
	 */
	public synchronized void buffed() {
		waxOn = false;
		notifyAll();
	}

	/**
	 * 等待打蜡
	 * @throws InterruptedException
	 */
	public synchronized void waitFoxWaxing() throws InterruptedException{
		while(waxOn == false){
			wait();
		}
	}

	/**
	 * 等待抛光
	 * @throws InterruptedException
	 */
	public synchronized void waitForBuffing() throws InterruptedException {
		while(waxOn ==true){
			wait();
		}
	} 
	
}

/**
 * 打蜡线程
 */
class WaxOn implements Runnable{
	private Car car;
	public WaxOn(Car car) {
		this.car = car;
	}
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){
				System.out.println("Wax on!");
				TimeUnit.MILLISECONDS.sleep(200);
				//打蜡工作
				car.waxed();
				//等待抛光
				car.waitForBuffing();
			}
		} catch (InterruptedException e) {
			System.out.println("Exiitng via interrupe "+this);
		}
		System.out.println("Ending Wax On task");
	}
}

/**
 * 打蜡结束
 */
class WaxOff implements Runnable{
	private Car car;
	public WaxOff(Car c) {
		this.car = c;
	}
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){
				//等待打蜡
				car.waitFoxWaxing();
				System.out.println("WaxOff!");
				TimeUnit.MILLISECONDS.sleep(200);
				//抛光
				car.buffed();
			}
		} catch (InterruptedException e) {
			System.out.println("Exiting via interrupt "+this);
		}
		System.out.println("Ending Wax Off task");
	}
}
public class WaxOMatic {
	public static void main(String[] args) throws InterruptedException {
		Car car = new Car();
		ExecutorService ex = Executors.newCachedThreadPool();
		ex.execute(new WaxOn(car));
		ex.execute(new WaxOff(car));
		TimeUnit.SECONDS.sleep(5);
		ex.shutdownNow();
	}
}
