package Daemon;

import java.util.concurrent.TimeUnit;

public class SimpleDaemons implements Runnable {

	@Override
	public void run() {
		try {
			//后台进程一直打印
			while (true) {
				TimeUnit.MILLISECONDS.sleep(100);
				System.out.println(Thread.currentThread() + " " + this);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("sleep interrupted");
		}
	}

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			Thread daemon = new Thread(new SimpleDaemons());
			//设置为后台线程
			daemon.setDaemon(true);
			daemon.start();
		}
		System.out.println("All daemons started");

		//主main线程sleep
		TimeUnit.MILLISECONDS.sleep(175);
	}

}
