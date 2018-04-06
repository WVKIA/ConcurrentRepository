package Exception;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

class ExceptionThread2 implements Runnable{
	public void run(){
		Thread t = Thread.currentThread();
		System.out.println("run()  by "+t);
		System.out.println("eh  = "+t.getUncaughtExceptionHandler());
		throw new RuntimeException();
	}
}

/**
 * 自定义异常处理器
 */
class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.out.println("Caught  "+e);
	}
	
}
/**
 * 实现线程工厂接口
 * 对每个新创建的线程做通用处理
 *
 */
class HandlerThreadFactory implements ThreadFactory{

	@Override
	public Thread newThread(Runnable r) {
		System.out.println(this+" creating new Thread===");
		Thread t = new Thread(r);
		System.out.println("created "+t+"===" );
		t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
		System.out.println(" eh = "+t.getUncaughtExceptionHandler());
		System.out.println("===========");
		return t;
	}
	
}
public class CaptureUncaughtException {
	public static void main(String[] args) {
		ExecutorService exce =Executors.newCachedThreadPool(new HandlerThreadFactory());
		exce.execute(new ExceptionThread2());
	}
}
