package Interrupted;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

//对sleep的阻塞
class SleepBlocked implements Runnable {
	@Override
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(100);
		} catch (InterruptedException e) {
			System.out.println("Interrupted Exception");
		}
		System.out.println("Exiting sleepBlocked run() ");
	}
}
//对IO的阻塞
class IOBlocked implements Runnable {
	private InputStream in;

	public IOBlocked(InputStream is) {
		this.in = is;
	}

	@Override
	public void run() {
		System.out.println("waiting to read(): ");
		try {
			in.read();
		} catch (IOException e) {
			e.printStackTrace();
			if (Thread.currentThread().isInterrupted()) {
				System.out.println("Interrupted from blocked IO");
			} else {
				throw new RuntimeException(e);
			}
		}
		System.out.println("Exiting IOBlocked run()");
	}
}
//对获取synchronized的阻塞
class SynchronizedBlocked implements Runnable {
	public synchronized void f() {
		while (true) {
			Thread.yield();
		}
	}
	//新起一个线程获取对象的锁，确保run'方法不能获取锁，从而达到阻塞的效果
	public SynchronizedBlocked() {
		new Thread() {
			public void run() {
				f();
			}
		}.start();
	}

	@Override
	public void run() {
		System.out.println("Trying to call f()");
		f();
		System.out.println("Exiting synchronizedBlocked run(*)");
	}
}

public class Interrupting {
	private static ExecutorService ex = Executors.newCachedThreadPool();

	static void test(Runnable r) throws InterruptedException {
		Future<?> f = ex.submit(r);		//通过submit获取一个线程上下文
		TimeUnit.MILLISECONDS.sleep(100);
		System.out.println("Interrupting " + r.getClass().getName());
		f.cancel(true);// interrupts if running	//调用cancled方法传递true，就会拥有这个线程上调用interupted以停止这个线程的权限，如果是运行的话就会中断。
		System.out.println("Interrupt sent to " + r.getClass().getName());
	}

	public static void main(String[] args) throws InterruptedException {
		test(new SleepBlocked());
		System.out.println("========================");
		test(new IOBlocked(System.in));
		System.out.println("==========================");
		test(new SynchronizedBlocked());
		System.out.println("=============================");
		TimeUnit.SECONDS.sleep(3);
		System.out.println("Aborting with system.exit(0)");
		System.exit(0);
		
		
	}
	
	//执行结果可以发现，sleep是可以中断的，但IO和syncronized块是无法中断的
}
