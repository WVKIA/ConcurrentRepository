package CriticalSection;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Accessor implements Runnable {
	private final int id;

	public Accessor(int id) {
		this.id = id;
	}

	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			ThreadLocalVariableHolder.increament();
			System.out.println(this);
			Thread.yield();
		}
	}

	@Override
	public String toString() {
		return "#" + id + ":" + ThreadLocalVariableHolder.get();
	}
}

public class ThreadLocalVariableHolder {
	//ThreadLocal对象，每个线程保留一个备份
	private static ThreadLocal<Integer> value = new ThreadLocal<Integer>() {
		private Random rand = new Random(47);

		protected synchronized Integer initialValue() {
			return rand.nextInt(10000);
		}
	};

	public static void increament() {
		value.set(value.get() + 1);
	}

	public static int get() {
		return value.get();
	}

	public static void main(String[] args) throws InterruptedException {
		ExecutorService exe = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			exe.execute(new Accessor(i));
		}
		TimeUnit.SECONDS.sleep(3);
		exe.shutdown();
	}
}
