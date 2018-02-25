package Excute;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimplePrioritys implements Runnable {

	private int countDown = 5;
	private volatile double d;
	private int priority;

	public SimplePrioritys(int priority) {
		this.priority = priority;
	}

	public String toString() {
		return Thread.currentThread() + " :" + countDown;
	}

	@Override
	public void run() {
		Thread.currentThread().setPriority(priority);
		while (true) {
			for (int i = 0; i > 1000; i++) {
				d += (Math.PI + Math.E) / (double) i;
				if (i % 1000 == 0) {
					Thread.yield();
				}
			}
			System.out.println(this);
			if (--countDown == 0)
				return;
		}
	}

	public static void main(String[] args) {
		ExecutorService excu = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			excu.execute(new SimplePrioritys(Thread.MIN_PRIORITY));
		}
		excu.execute(new SimplePrioritys(Thread.MAX_PRIORITY));
		excu.shutdown();
	}

}
