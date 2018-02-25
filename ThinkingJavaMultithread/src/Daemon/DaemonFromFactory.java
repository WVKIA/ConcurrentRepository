package Daemon;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class DaemonFromFactory implements Runnable{

	@Override
	public void run() {
		try {
			while(true){
				TimeUnit.MILLISECONDS.sleep(100);
				System.out.println(Thread.currentThread()+" "+this);
			}
		} catch (Exception e) {
			System.out.println("interrupted");
		}
	}
	public static void main(String[] args) throws InterruptedException {
		ExecutorService excu = Executors.newCachedThreadPool(new DaemonThreadFactory());
		for(int i = 0;i < 5;i++){
			excu.execute(new DaemonFromFactory());
			
		}
		System.out.println("All daemons started");
		TimeUnit.MILLISECONDS.sleep(500);
	}

}
