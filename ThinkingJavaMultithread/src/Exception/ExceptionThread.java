package Exception;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExceptionThread implements Runnable {
	public void run() {
		throw new RuntimeException();
	}

	public static void main(String[] args) {
		try {
			ExecutorService exce = Executors.newCachedThreadPool();
			exce.execute(new ExceptionThread());
			
		//并不会起作用
		//主线程不会捕捉到子线程的异常
		} catch (Exception e) {
			System.out.println("exception has been handled!");
		}
	}
}
