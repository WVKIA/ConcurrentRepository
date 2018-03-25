package ExcuteAndCallable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import LiftOff.LiftOff;

public class SleepingTask extends LiftOff {
	public void run() {
		try {
			while (countDown-- > 0) {
				System.out.println(status());
				// old style
				// Thread.sleep(10)
				// java se5/6-style
				TimeUnit.MILLISECONDS.sleep(100);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.err.println("interrupted");
		}
	}

	public static void main(String[] args) {
		ExecutorService excu = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			excu.execute(new SleepingTask());
		}

		excu.shutdown();
	}
}
